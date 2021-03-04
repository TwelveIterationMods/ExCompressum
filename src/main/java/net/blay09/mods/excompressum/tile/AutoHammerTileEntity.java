package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.block.AutoHammerBlock;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.container.AutoHammerContainer;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.hammer.HammerRegistry;
import net.blay09.mods.excompressum.utils.*;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public class AutoHammerTileEntity extends BaseTileEntity implements ITickableTileEntity, INamedContainerProvider {

    private static final int UPDATE_INTERVAL = 20;

    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(32000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!simulate) {
                isDirty = true;
            }
            return super.receiveEnergy(maxReceive, simulate);
        }
    };


    private final DefaultItemHandler itemHandler = new DefaultItemHandler(this, 23) {
        @Override
        public boolean isItemValid(int slot, ItemStack itemStack) {
            if (slot == 0) {
                return isRegistered(itemStack);
            } else if (slot == 21 || slot == 22) {
                return isHammerUpgrade(itemStack);
            }
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            // Make sure the hammer slots are always synced.
            if (hammerSlots.isInside(slot)) {
                isDirty = true;
            }
        }
    };
    private final SubItemHandler inputSlots = new SubItemHandler(itemHandler, 0, 1);
    private final SubItemHandler outputSlots = new SubItemHandler(itemHandler, 1, 21);
    private final SubItemHandler hammerSlots = new SubItemHandler(itemHandler, 21, 23);
    private final ItemHandlerAutomation itemHandlerAutomation = new ItemHandlerAutomation(itemHandler) {
        @Override
        public boolean canExtractItem(int slot, int amount) {
            return super.canExtractItem(slot, amount) && outputSlots.isInside(slot);
        }

        @Override
        public boolean canInsertItem(int slot, ItemStack itemStack) {
            return super.canInsertItem(slot, itemStack) && inputSlots.isInside(slot) || hammerSlots.isInside(slot);
        }
    };

    private final LazyOptional<EnergyStorage> energyStorageCap = LazyOptional.of(() -> energyStorage);
    private final LazyOptional<ItemHandlerAutomation> itemHandlerAutomationCap = LazyOptional.of(() -> itemHandlerAutomation);

    private ItemStack currentStack = ItemStack.EMPTY;
    private int cooldown;

    private int ticksSinceUpdate;
    private boolean isDirty;
    private float progress;

    private ItemStack finishedStack = ItemStack.EMPTY;
    public float hammerAngle;

    private boolean isDisabledByRedstone;

    public AutoHammerTileEntity() {
        this(ModTileEntities.autoHammer);
    }

    public AutoHammerTileEntity(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        if (!world.isRemote) {
            if (cooldown > 0) {
                cooldown--;
            }
            int effectiveEnergy = getEffectiveEnergy();
            if (!isDisabledByRedstone() && getEnergyStored() >= effectiveEnergy) {
                if (currentStack.isEmpty() && cooldown <= 0) {
                    ItemStack inputStack = inputSlots.getStackInSlot(0);
                    if (!inputStack.isEmpty() && isRegistered(inputStack)) {
                        boolean foundSpace = false;
                        for (int i = 0; i < outputSlots.getSlots(); i++) {
                            if (outputSlots.getStackInSlot(i).isEmpty()) {
                                foundSpace = true;
                            }
                        }
                        if (!foundSpace) {
                            return;
                        }
                        currentStack = inputStack.split(1);
                        if (inputStack.isEmpty()) {
                            inputSlots.setStackInSlot(0, ItemStack.EMPTY);
                        }
                        energyStorage.extractEnergy(effectiveEnergy, false);
                        ticksSinceUpdate = UPDATE_INTERVAL;
                        progress = 0f;
                    }
                } else {
                    energyStorage.extractEnergy(effectiveEnergy, false);
                    progress += getEffectiveSpeed();
                    isDirty = true;
                    if (progress >= 1) {
                        if (!world.isRemote) {
                            if (world.rand.nextFloat() <= ExCompressumConfig.COMMON.autoHammerDecay.get()) {
                                ItemStack firstHammer = hammerSlots.getStackInSlot(0);
                                if (!firstHammer.isEmpty()) {
                                    if (firstHammer.attemptDamageItem(1, world.rand, null)) {
                                        hammerSlots.setStackInSlot(0, ItemStack.EMPTY);
                                    }
                                }
                                ItemStack secondHammer = hammerSlots.getStackInSlot(1);
                                if (!secondHammer.isEmpty()) {
                                    if (secondHammer.attemptDamageItem(1, world.rand, null)) {
                                        hammerSlots.setStackInSlot(1, ItemStack.EMPTY);
                                    }
                                }
                            }
                            Collection<ItemStack> rewards = rollHammerRewards(currentStack, getEffectiveTool(), world.rand);
                            for (ItemStack itemStack : rewards) {
                                if (!addItemToOutput(itemStack)) {
                                    ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemStack);
                                    double motion = 0.05;
                                    entityItem.setMotion(world.rand.nextGaussian() * motion, 0.2, world.rand.nextGaussian() * motion);
                                    world.addEntity(entityItem);
                                }
                            }
                        }
                        finishedStack = currentStack;
                        progress = 0f;
                        ticksSinceUpdate = UPDATE_INTERVAL;
                        cooldown = 2;
                        currentStack = ItemStack.EMPTY;
                    }
                }
            }

            // Sync to clients
            ticksSinceUpdate++;
            if (ticksSinceUpdate > UPDATE_INTERVAL) {
                if (isDirty) {
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                    finishedStack = ItemStack.EMPTY;
                    isDirty = false;
                }
                ticksSinceUpdate = 0;
            }
        }

        if (world.isRemote && !finishedStack.isEmpty()) {
            BlockState state = StupidUtils.getStateFromItemStack(finishedStack);
            if (state != null) {
                ExCompressum.proxy.spawnCrushParticles(world, pos, state);
            }
            finishedStack = ItemStack.EMPTY;
        }
    }

    private ItemStack getEffectiveTool() {
        return Math.random() < 0.5 ? hammerSlots.getStackInSlot(0) : hammerSlots.getStackInSlot(1);
    }

    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    private boolean addItemToOutput(ItemStack itemStack) {
        int firstEmptySlot = -1;
        for (int i = 0; i < outputSlots.getSlots(); i++) {
            ItemStack slotStack = outputSlots.getStackInSlot(i);
            if (slotStack.isEmpty()) {
                if (firstEmptySlot == -1) {
                    firstEmptySlot = i;
                }
            } else {
                if (slotStack.getCount() + itemStack.getCount() <= slotStack.getMaxStackSize() && slotStack.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(slotStack, itemStack)) {
                    slotStack.grow(itemStack.getCount());
                    return true;
                }
            }
        }
        if (firstEmptySlot != -1) {
            outputSlots.setStackInSlot(firstEmptySlot, itemStack);
            return true;
        }
        return false;
    }

    public int getEffectiveEnergy() {
        return ExCompressumConfig.COMMON.autoHammerEnergy.get();
    }

    public float getSpeedMultiplier() {
        final float HAMMER_BOOST = 0.5f;
        final float EFFICIENCY_BOOST = 0.5f;
        float boost = 1f;
        ItemStack firstHammer = hammerSlots.getStackInSlot(0);
        if (!firstHammer.isEmpty() && isHammerUpgrade(firstHammer)) {
            boost += HAMMER_BOOST;
            boost += EFFICIENCY_BOOST * EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, firstHammer);
        }
        ItemStack secondHammer = hammerSlots.getStackInSlot(1);
        if (!secondHammer.isEmpty() && isHammerUpgrade(secondHammer)) {
            boost += HAMMER_BOOST;
            boost += EFFICIENCY_BOOST * EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, secondHammer);
        }
        return boost;
    }

    public float getEffectiveSpeed() {
        return (float) (ExCompressumConfig.COMMON.autoHammerSpeed.get() * getSpeedMultiplier());
    }

    public float getEffectiveLuck() {
        float luck = 0f;
        ItemStack firstHammer = hammerSlots.getStackInSlot(0);
        if (!firstHammer.isEmpty() && isHammerUpgrade(firstHammer)) {
            luck += EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, firstHammer);
        }
        ItemStack secondHammer = hammerSlots.getStackInSlot(1);
        if (!secondHammer.isEmpty() && isHammerUpgrade(secondHammer)) {
            luck += EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, secondHammer);
        }
        return luck;
    }

    @Override
    protected boolean hasUpdatePacket() {
        return true;
    }

    @Override
    protected void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        currentStack = ItemStack.read(tagCompound.getCompound("CurrentStack"));
        progress = tagCompound.getFloat("Progress");
        if (tagCompound.contains("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.get("EnergyStorage"));
        }

        if (isSync) {
            hammerSlots.setStackInSlot(0, ItemStack.read(tagCompound.getCompound("FirstHammer")));
            hammerSlots.setStackInSlot(1, ItemStack.read(tagCompound.getCompound("SecondHammer")));
        } else {
            itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        }

        isDisabledByRedstone = tagCompound.getBoolean("IsDisabledByRedstone");
        if (tagCompound.contains("FinishedStack")) {
            finishedStack = ItemStack.read(tagCompound.getCompound("FinishedStack"));
        }
    }

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        INBT energyStorageNBT = CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
        if (energyStorageNBT != null) {
            tagCompound.put("EnergyStorage", energyStorageNBT);
        }

        tagCompound.put("CurrentStack", currentStack.write(new CompoundNBT()));
        tagCompound.putFloat("Progress", progress);
        if (isSync) {
            ItemStack firstHammer = hammerSlots.getStackInSlot(0);
            tagCompound.put("FirstHammer", firstHammer.write(new CompoundNBT()));
            ItemStack secondHammer = hammerSlots.getStackInSlot(1);
            tagCompound.put("SecondHammer", secondHammer.write(new CompoundNBT()));
        } else {
            tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        }

        tagCompound.putBoolean("IsDisabledByRedstone", isDisabledByRedstone);

        if (!finishedStack.isEmpty()) {
            tagCompound.put("FinishedStack", finishedStack.write(new CompoundNBT()));
        }
    }

    public boolean isProcessing() {
        return progress > 0f;
    }

    public float getProgress() {
        return progress;
    }

    public float getEnergyPercentage() {
        return (float) getEnergyStored() / (float) getMaxEnergyStored();
    }

    public ItemStack getCurrentStack() {
        return currentStack;
    }

    @Nullable
    public BlockState getCurrentBlock() {
        return StupidUtils.getStateFromItemStack(currentStack);
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (LazyOptional<T>) itemHandlerAutomationCap;
        } else if (cap == CapabilityEnergy.ENERGY) {
            return (LazyOptional<T>) energyStorageCap;
        }
        return super.getCapability(cap, side);
    }

    public DefaultItemHandler getItemHandler() {
        return itemHandler;
    }

    public ItemStack getUpgradeStack(int i) {
        return hammerSlots.getStackInSlot(i);
    }

    public boolean isHammerUpgrade(ItemStack itemStack) {
        if (itemStack.getItem() == Compat.TCONSTRUCT_HAMMER) {
            CompoundNBT tagCompound = itemStack.getTag();
            if (tagCompound != null) {
                ListNBT traits = tagCompound.getList("Traits", Constants.NBT.TAG_STRING);
                for (INBT tag : traits) {
                    if (tag.getString().equalsIgnoreCase(Compat.TCONSTRUCT_TRAIT_SMASHING)) {
                        return true;
                    }
                }
            }
        }
        return ExNihilo.isNihiloItem(itemStack, ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
    }

    public boolean isRegistered(ItemStack itemStack) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(world);
        return ExNihilo.isHammerable(itemStack) || ExRegistries.getHammerRegistry().isHammerable(recipeManager, itemStack);
    }

    public Collection<ItemStack> rollHammerRewards(ItemStack itemStack, ItemStack toolItem, Random rand) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(world);
        if (ExRegistries.getHammerRegistry().isHammerable(recipeManager, itemStack)) {
            LootContext lootContext = LootTableUtils.buildLootContext(((ServerWorld) world), itemStack, rand);
            return HammerRegistry.rollHammerRewards(lootContext, itemStack);
        }

        BlockState currentState = StupidUtils.getStateFromItemStack(itemStack);
        return ExNihilo.getInstance().rollHammerRewards(currentState, toolItem, rand);
    }

    public int getMiningLevel() {
        return ItemTier.DIAMOND.getHarvestLevel();
    }

    public boolean shouldAnimate() {
        return !currentStack.isEmpty() && getEnergyStored() >= getEffectiveEnergy() && !isDisabledByRedstone();
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return energyStorage;
    }

    public boolean isUgly() {
        BlockState state = getBlockState();
        if (state.hasProperty(AutoHammerBlock.UGLY)) {
            return state.get(AutoHammerBlock.UGLY);
        }
        return false;
    }

    public Direction getFacing() {
        BlockState state = getBlockState();
        if (state.hasProperty(AutoHammerBlock.FACING)) {
            return state.get(AutoHammerBlock.FACING);
        }
        return Direction.NORTH;
    }

    public boolean isDisabledByRedstone() {
        return isDisabledByRedstone;
    }

    public void setDisabledByRedstone(boolean disabledByRedstone) {
        isDisabledByRedstone = disabledByRedstone;
        isDirty = true;
        ticksSinceUpdate = UPDATE_INTERVAL;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.excompressum.auto_hammer");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        return new AutoHammerContainer(windowId, inv, this);
    }

}
