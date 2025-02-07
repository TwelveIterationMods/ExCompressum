package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.container.DelegateContainer;
import net.blay09.mods.balm.api.container.SubContainer;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.AutoHammerBlock;
import net.blay09.mods.excompressum.block.ModBlockStateProperties;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.menu.AutoHammerMenu;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.hammer.HammerRegistry;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.blay09.mods.excompressum.utils.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AutoHammerBlockEntity extends AbstractBaseBlockEntity implements BalmMenuProvider, BalmContainerProvider, BalmEnergyStorageProvider {

    private static final int UPDATE_INTERVAL = 20;

    private final EnergyStorage energyStorage = new EnergyStorage(32000) {
        @Override
        public int fill(int maxReceive, boolean simulate) {
            if (!simulate) {
                isDirty = true;
            }
            return super.fill(maxReceive, simulate);
        }
    };

    private final ContainerData containerData = new ContainerData() {
        public int get(int id) {
            if (id == 0) {
                return (int) (100f * AutoHammerBlockEntity.this.getProgress());
            } else if (id == 1) {
                return AutoHammerBlockEntity.this.getEnergyStorage().getEnergy();
            } else if (id == 2) {
                return AutoHammerBlockEntity.this.isDisabledByRedstone() ? 1 : 0;
            }
            return 0;
        }

        public void set(int id, int value) {
            if (id == 0) {
                AutoHammerBlockEntity.this.setProgress(value / 100f);
            } else if (id == 1) {
                AutoHammerBlockEntity.this.getEnergyStorage().setEnergy(value);
            } else if (id == 2) {
                AutoHammerBlockEntity.this.setDisabledByRedstone(value == 1);
            }
        }

        public int getCount() {
            return 3;
        }
    };

    private final DefaultContainer backingContainer = new DefaultContainer(23) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            if (inputSlots.containsOuterSlot(slot)) {
                return isRegistered(itemStack);
            } else if (hammerSlots.containsOuterSlot(slot)) {
                return isHammerUpgrade(itemStack);
            }
            return true;
        }

        @Override
        public void slotChanged(int slot) {
            super.slotChanged(slot);
            // Make sure the hammer slots are always synced.
            if (hammerSlots.containsOuterSlot(slot)) {
                isDirty = true;
            }
        }
    };
    private final SubContainer inputSlots = new SubContainer(backingContainer, 0, 1);
    private final SubContainer outputSlots = new SubContainer(backingContainer, 1, 21);
    private final SubContainer hammerSlots = new SubContainer(backingContainer, 21, 23);
    private final List<ItemStack> overflowBuffer = new ArrayList<>();
    private final DelegateContainer container = new DelegateContainer(backingContainer) {
        @Override
        public ItemStack removeItem(int slot, int count) {
            if (!outputSlots.containsOuterSlot(slot)) {
                return ItemStack.EMPTY;
            }

            return super.removeItem(slot, count);
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            if (!outputSlots.containsOuterSlot(slot)) {
                return ItemStack.EMPTY;
            }

            return super.removeItemNoUpdate(slot);
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            return super.canPlaceItem(slot, itemStack)
                    && (inputSlots.containsOuterSlot(slot) || hammerSlots.containsOuterSlot(slot));
        }

        @Override
        public boolean canExtractItem(int slot) {
            return outputSlots.containsOuterSlot(slot);
        }
    };

    private ItemStack currentStack = ItemStack.EMPTY;
    private int cooldown;

    private int ticksSinceUpdate;
    private boolean isDirty;
    private float progress;

    private ItemStack finishedStack = ItemStack.EMPTY;
    public float hammerAngle;

    private boolean isDisabledByRedstone;

    public AutoHammerBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.autoHammer.get(), pos, state);
    }

    public AutoHammerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, AutoHammerBlockEntity blockEntity) {
        blockEntity.clientTick();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AutoHammerBlockEntity blockEntity) {
        blockEntity.serverTick();
    }

    public void clientTick() {
        if (!finishedStack.isEmpty()) {
            BlockState state = StupidUtils.getStateFromItemStack(finishedStack);
            if (!state.isAir()) {
                ExCompressum.proxy.get().spawnCrushParticles(level, worldPosition, state);
            }
            finishedStack = ItemStack.EMPTY;
        }
    }

    public void serverTick() {
        if (cooldown > 0) {
            cooldown--;
        }

        int effectiveEnergy = getEffectiveEnergy();
        if (!isDisabledByRedstone() && overflowBuffer.isEmpty() && getEnergyStored() >= effectiveEnergy) {
            if (currentStack.isEmpty() && cooldown <= 0) {
                ItemStack inputStack = inputSlots.getItem(0);
                if (!inputStack.isEmpty() && isRegistered(inputStack)) {
                    boolean foundSpace = false;
                    for (int i = 0; i < outputSlots.getContainerSize(); i++) {
                        if (outputSlots.getItem(i).isEmpty()) {
                            foundSpace = true;
                        }
                    }
                    if (!foundSpace) {
                        return;
                    }
                    currentStack = inputStack.split(1);
                    if (inputStack.isEmpty()) {
                        inputSlots.setItem(0, ItemStack.EMPTY);
                    }
                    energyStorage.drain(effectiveEnergy, false);
                    ticksSinceUpdate = UPDATE_INTERVAL;
                    progress = 0f;
                }
            } else {
                energyStorage.drain(effectiveEnergy, false);
                progress += getEffectiveSpeed();
                isDirty = true;
                if (progress >= 1) {
                    if (!level.isClientSide) {
                        if (level.random.nextFloat() <= ExCompressumConfig.getActive().automation.autoHammerDecay) {
                            ItemStack firstHammer = hammerSlots.getItem(0);
                            if (!firstHammer.isEmpty()) {
                                if (firstHammer.hurt(1, level.random, null)) {
                                    hammerSlots.setItem(0, ItemStack.EMPTY);
                                }
                            }
                            ItemStack secondHammer = hammerSlots.getItem(1);
                            if (!secondHammer.isEmpty()) {
                                if (secondHammer.hurt(1, level.random, null)) {
                                    hammerSlots.setItem(1, ItemStack.EMPTY);
                                }
                            }
                        }
                        Collection<ItemStack> rewards = rollHammerRewards(currentStack, getEffectiveTool(), level.random);
                        for (ItemStack itemStack : rewards) {
                            if (!addItemToOutput(itemStack)) {
                                overflowBuffer.add(itemStack);
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
        } else if (!overflowBuffer.isEmpty()) {
            if (addItemToOutput(overflowBuffer.get(0))) {
                overflowBuffer.remove(0);
            }
        }

        // Sync to clients
        ticksSinceUpdate++;
        if (ticksSinceUpdate > UPDATE_INTERVAL) {
            if (isDirty) {
                sync();
                finishedStack = ItemStack.EMPTY;
                isDirty = false;
            }
            ticksSinceUpdate = 0;
        }
    }

    private ItemStack getEffectiveTool() {
        return Math.random() < 0.5 ? hammerSlots.getItem(0) : hammerSlots.getItem(1);
    }

    public int getEnergyStored() {
        return energyStorage.getEnergy();
    }

    public int getMaxEnergyStored() {
        return energyStorage.getCapacity();
    }

    private boolean addItemToOutput(ItemStack itemStack) {
        int firstEmptySlot = -1;
        for (int i = 0; i < outputSlots.getContainerSize(); i++) {
            ItemStack slotStack = outputSlots.getItem(i);
            if (slotStack.isEmpty()) {
                if (firstEmptySlot == -1) {
                    firstEmptySlot = i;
                }
            } else {
                if (slotStack.getCount() + itemStack.getCount() <= slotStack.getMaxStackSize() && ItemStack.isSameItemSameTags(slotStack, itemStack)) {
                    slotStack.grow(itemStack.getCount());
                    return true;
                }
            }
        }
        if (firstEmptySlot != -1) {
            outputSlots.setItem(firstEmptySlot, itemStack);
            return true;
        }
        return false;
    }

    public int getEffectiveEnergy() {
        return ExCompressumConfig.getActive().automation.autoHammerEnergy;
    }

    public float getSpeedMultiplier() {
        final float HAMMER_BOOST = 0.5f;
        final float EFFICIENCY_BOOST = 0.5f;
        float boost = 1f;
        ItemStack firstHammer = hammerSlots.getItem(0);
        if (!firstHammer.isEmpty() && isHammerUpgrade(firstHammer)) {
            boost += HAMMER_BOOST;
            boost += EFFICIENCY_BOOST * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, firstHammer);
        }
        ItemStack secondHammer = hammerSlots.getItem(1);
        if (!secondHammer.isEmpty() && isHammerUpgrade(secondHammer)) {
            boost += HAMMER_BOOST;
            boost += EFFICIENCY_BOOST * EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY, secondHammer);
        }
        return boost;
    }

    public float getEffectiveSpeed() {
        return (float) (ExCompressumConfig.getActive().automation.autoHammerSpeed * getSpeedMultiplier());
    }

    public float getEffectiveLuck() {
        float luck = 0f;
        ItemStack firstHammer = hammerSlots.getItem(0);
        if (!firstHammer.isEmpty() && isHammerUpgrade(firstHammer)) {
            luck += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, firstHammer);
        }
        ItemStack secondHammer = hammerSlots.getItem(1);
        if (!secondHammer.isEmpty() && isHammerUpgrade(secondHammer)) {
            luck += EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, secondHammer);
        }
        return luck;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        currentStack = ItemStack.of(tag.getCompound("CurrentStack"));
        progress = tag.getFloat("Progress");
        if (tag.contains("EnergyStorage")) {
            energyStorage.deserialize(tag.get("EnergyStorage"));
        }

        backingContainer.deserialize(tag.getCompound("ItemHandler"));

        isDisabledByRedstone = tag.getBoolean("IsDisabledByRedstone");
        if (tag.contains("FinishedStack")) {
            finishedStack = ItemStack.of(tag.getCompound("FinishedStack"));
        }

        if (tag.contains("FirstHammer", Tag.TAG_COMPOUND)) {
            hammerSlots.setItem(0, ItemStack.of(tag.getCompound("FirstHammer")));
        }
        if (tag.contains("SecondHammer", Tag.TAG_COMPOUND)) {
            hammerSlots.setItem(1, ItemStack.of(tag.getCompound("SecondHammer")));
        }
        overflowBuffer.clear();
        for (final var overflowItem : tag.getList("OverflowBuffer", Tag.TAG_COMPOUND)) {
            overflowBuffer.add(ItemStack.of(((CompoundTag) overflowItem)));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("EnergyStorage", energyStorage.serialize());

        tag.put("CurrentStack", currentStack.save(new CompoundTag()));
        tag.putFloat("Progress", progress);
        tag.put("ItemHandler", backingContainer.serialize());

        tag.putBoolean("IsDisabledByRedstone", isDisabledByRedstone);

        if (!finishedStack.isEmpty()) {
            tag.put("FinishedStack", finishedStack.save(new CompoundTag()));
        }

        final var overflowList = new ListTag();
        for (ItemStack itemStack : overflowBuffer) {
            overflowList.add(itemStack.save(new CompoundTag()));
        }
        tag.put("OverflowBuffer", overflowList);
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
        ItemStack firstHammer = hammerSlots.getItem(0);
        tag.put("FirstHammer", firstHammer.save(new CompoundTag()));
        ItemStack secondHammer = hammerSlots.getItem(1);
        tag.put("SecondHammer", secondHammer.save(new CompoundTag()));
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

    public DefaultContainer getBackingContainer() {
        return backingContainer;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    public ItemStack getUpgradeStack(int i) {
        return hammerSlots.getItem(i);
    }

    public boolean isHammerUpgrade(ItemStack itemStack) {
        return itemStack.is(ModItemTags.HAMMERS);
    }

    public boolean isRegistered(ItemStack itemStack) {
        if (level == null) {
            return false;
        }

        final var recipeManager = level.getRecipeManager();
        return ExNihilo.isHammerable(level, itemStack) || ExRegistries.getHammerRegistry().isHammerable(recipeManager, itemStack);
    }

    public Collection<ItemStack> rollHammerRewards(ItemStack itemStack, ItemStack toolItem, RandomSource rand) {
        if (level == null) {
            return Collections.emptyList();
        }

        final var recipeManager = level.getRecipeManager();
        if (ExRegistries.getHammerRegistry().isHammerable(recipeManager, itemStack)) {
            LootContext lootContext = LootTableUtils.buildLootContext(((ServerLevel) level), itemStack);
            return HammerRegistry.rollHammerRewards(lootContext, itemStack);
        }

        BlockState currentState = StupidUtils.getStateFromItemStack(itemStack);
        return ExNihilo.getInstance().rollHammerRewards(level, currentState, toolItem, rand);
    }

    public int getMiningLevel() {
        return Tiers.DIAMOND.getLevel();
    }

    public boolean shouldAnimate() {
        return !currentStack.isEmpty() && getEnergyStored() >= getEffectiveEnergy() && !isDisabledByRedstone();
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public boolean isUgly() {
        BlockState state = getBlockState();
        if (state.hasProperty(ModBlockStateProperties.UGLY)) {
            return state.getValue(ModBlockStateProperties.UGLY);
        }
        return false;
    }

    public Direction getFacing() {
        BlockState state = getBlockState();
        if (state.hasProperty(AutoHammerBlock.FACING)) {
            return state.getValue(AutoHammerBlock.FACING);
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
    public Component getDisplayName() {
        return Component.translatable("block.excompressum.auto_hammer");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inventory, Player player) {
        return new AutoHammerMenu(windowId, inventory, this);
    }

    public ContainerData getContainerData() {
        return containerData;
    }
}
