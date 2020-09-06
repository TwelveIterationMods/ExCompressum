package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.block.BlockAutoHammer;
import net.blay09.mods.excompressum.client.render.ParticleAutoHammer;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.utils.DefaultItemHandler;
import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.blay09.mods.excompressum.utils.ItemHandlerAutomation;
import net.blay09.mods.excompressum.utils.SubItemHandler;
import net.minecraft.block.Block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public class AutoHammerTileEntity extends BaseTileEntity implements ITickable {

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

    private ItemStack currentStack = ItemStack.EMPTY;

    private int ticksSinceUpdate;
    private boolean isDirty;
    private float progress;

    private BlockState cachedState;
    public float hammerAngle;

    private boolean isDisabledByRedstone;

    @Override
    public void update() {
        if (!world.isRemote) {
            int effectiveEnergy = getEffectiveEnergy();
            if (!isDisabledByRedstone() && getEnergyStored(null) >= effectiveEnergy) {
                if (currentStack.isEmpty()) {
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
                        VanillaPacketHandler.sendTileEntityUpdate(this);
                        progress = 0f;
                    }
                } else {
                    energyStorage.extractEnergy(effectiveEnergy, false);
                    progress += getEffectiveSpeed();
                    isDirty = true;
                    if (progress >= 1) {
                        if (!world.isRemote) {
                            if (world.rand.nextFloat() <= ModConfig.automation.autoHammerDecay) {
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
                            Collection<ItemStack> rewards = rollHammerRewards(currentStack, getMiningLevel(), getEffectiveLuck(), world.rand);
                            for (ItemStack itemStack : rewards) {
                                if (!addItemToOutput(itemStack)) {
                                    ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemStack);
                                    double motion = 0.05;
                                    entityItem.setMotion(world.rand.nextGaussian() * motion, 0.2, world.rand.nextGaussian() * motion);
                                    world.addEntity(entityItem);
                                }
                            }
                        } else {
                            spawnCrushParticles();
                        }
                        progress = 0f;
                        currentStack = ItemStack.EMPTY;
                    }
                }
            }

            // Sync to clients
            ticksSinceUpdate++;
            if (ticksSinceUpdate > UPDATE_INTERVAL) {
                if (isDirty) {
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                    isDirty = false;
                }
                ticksSinceUpdate = 0;
            }
        }
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
        return ModConfig.automation.autoHammerEnergy;
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
        return ModConfig.automation.autoHammerSpeed * getSpeedMultiplier();
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
        currentStack = new ItemStack(tagCompound.getCompound("CurrentStack"));
        progress = tagCompound.getFloat("Progress");
        if (tagCompound.contains("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.get("EnergyStorage"));
        }

        if (isSync) {
            hammerSlots.setStackInSlot(0, new ItemStack(tagCompound.getCompound("FirstHammer")));
            hammerSlots.setStackInSlot(1, new ItemStack(tagCompound.getCompound("SecondHammer")));
        } else {
            itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        }

        isDisabledByRedstone = tagCompound.getBoolean("IsDisabledByRedstone");
    }

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        INBT energyStorageNBT = CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
        if (energyStorageNBT != null) {
            tagCompound.put("EnergyStorage", energyStorageNBT);
        }

        tagCompound.put("CurrentStack", currentStack.writeToNBT(new CompoundNBT()));
        tagCompound.putFloat("Progress", progress);
        if (isSync) {
            ItemStack firstHammer = hammerSlots.getStackInSlot(0);
            tagCompound.put("FirstHammer", firstHammer.writeToNBT(new CompoundNBT()));
            ItemStack secondHammer = hammerSlots.getStackInSlot(1);
            tagCompound.put("SecondHammer", secondHammer.writeToNBT(new CompoundNBT()));
        } else {
            tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        }

        tagCompound.putBoolean("IsDisabledByRedstone", isDisabledByRedstone);
    }

    @OnlyIn(Dist.CLIENT)
    private void spawnCrushParticles() {
        if (!ModConfig.client.disableParticles && !isUgly()) {
            BlockState currentBlock = getCurrentBlock();
            if (currentBlock != null) {
                for (int i = 0; i < 10; i++) {
                    Minecraft.getInstance().effectRenderer.addEffect(new ParticleAutoHammer(world, pos, pos.getX() + 0.7f, pos.getY() + 0.3f, pos.getZ() + 0.5f, (-world.rand.nextDouble() + 0.2f) / 9, 0.2f, (world.rand.nextDouble() - 0.5) / 9, currentBlock));
                }
            }
        }
    }

    public boolean isProcessing() {
        return progress > 0f;
    }

    public float getProgress() {
        return progress;
    }

    public float getEnergyPercentage() {
        return (float) getEnergyStored(null) / (float) getMaxEnergyStored(null);
    }

    public ItemStack getCurrentStack() {
        return currentStack;
    }

    @Nullable
    @SuppressWarnings("deprecation")
    public BlockState getCurrentBlock() {
        if (currentStack.isEmpty()) {
            return null;
        }
        Block block = Block.getBlockFromItem(currentStack.getItem());
        if (block != Blocks.AIR) {
            return block.getStateFromMeta(currentStack.getMetadata());
        }
        return null;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandlerAutomation;
        }
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
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
        return ExRegistro.isNihiloItem(itemStack, ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
    }

    public boolean isRegistered(ItemStack itemStack) {
        return ExRegistro.isHammerable(itemStack);
    }

    public Collection<ItemStack> rollHammerRewards(ItemStack itemStack, int miningLevel, float luck, Random rand) {
        return ExRegistro.rollHammerRewards(itemStack, miningLevel, luck, rand);
    }

    public int getMiningLevel() {
        return Item.ToolMaterial.DIAMOND.getHarvestLevel();
    }

    public boolean shouldAnimate() {
        return !currentStack.isEmpty() && getEnergyStored(null) >= getEffectiveEnergy() && !isDisabledByRedstone();
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, BlockState oldState, BlockState newState) {
        cachedState = null;
        return oldState.getBlock() != newState.getBlock();
    }

    public boolean isUgly() {
        if (cachedState == null) {
            cachedState = world.getBlockState(pos);
        }
        if (cachedState.getBlock() instanceof BlockAutoHammer) {
            return cachedState.get(BlockAutoHammer.UGLY);
        }
        return false;
    }

    public Direction getFacing() {
        if (cachedState == null) {
            cachedState = world.getBlockState(pos);
        }
        if (cachedState.getBlock() instanceof BlockAutoHammer) {
            return cachedState.get(BlockAutoHammer.FACING);
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
}
