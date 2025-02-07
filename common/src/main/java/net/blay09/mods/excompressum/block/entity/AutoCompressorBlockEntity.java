package net.blay09.mods.excompressum.block.entity;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.blay09.mods.balm.api.container.BalmContainerProvider;
import net.blay09.mods.balm.api.container.DefaultContainer;
import net.blay09.mods.balm.api.container.DelegateContainer;
import net.blay09.mods.balm.api.container.SubContainer;
import net.blay09.mods.balm.api.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.api.energy.EnergyStorage;
import net.blay09.mods.balm.api.menu.BalmMenuProvider;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.menu.AutoCompressorMenu;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class AutoCompressorBlockEntity extends AbstractBaseBlockEntity implements BalmMenuProvider, BalmEnergyStorageProvider, BalmContainerProvider {

    private final EnergyStorage energyStorage = new EnergyStorage(32000) {
        @Override
        public int fill(int maxReceive, boolean simulate) {
            if (!simulate) {
                setChanged();
            }
            return super.fill(maxReceive, simulate);
        }
    };


    private final Multiset<CompressedRecipe> inputItems = HashMultiset.create();
    private final DefaultContainer backingContainer = new DefaultContainer(24) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            return slot >= 12 || ExRegistries.getCompressedRecipeRegistry().getRecipe(itemStack) != null;
        }
    };
    private final SubContainer inputSlots = new SubContainer(backingContainer, 0, 12);
    private final SubContainer outputSlots = new SubContainer(backingContainer, 12, 24);
    private final List<ItemStack> overflowBuffer = new ArrayList<>();
    private final DelegateContainer container = new DelegateContainer(backingContainer) {
        @Override
        public ItemStack removeItem(int slot, int count) {
            if (slot < 12) {
                return ItemStack.EMPTY;
            }

            return super.removeItem(slot, count);
        }

        @Override
        public ItemStack removeItemNoUpdate(int slot) {
            if (slot < 12) {
                return ItemStack.EMPTY;
            }

            return super.removeItemNoUpdate(slot);
        }

        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            return slot < 12 && ExRegistries.getCompressedRecipeRegistry().getRecipe(itemStack) != null;
        }
    };

    private final ContainerData containerData = new ContainerData() {
        public int get(int id) {
            if (id == 0) {
                return (int) (100f * AutoCompressorBlockEntity.this.getProgress());
            } else if (id == 1) {
                return AutoCompressorBlockEntity.this.getEnergyStorage().getEnergy();
            } else if (id == 2) {
                return AutoCompressorBlockEntity.this.isDisabledByRedstone() ? 1 : 0;
            }
            return 0;
        }

        public void set(int id, int value) {
            if (id == 0) {
                AutoCompressorBlockEntity.this.setProgress(value / 100f);
            } else if (id == 1) {
                AutoCompressorBlockEntity.this.getEnergyStorage().setEnergy(value);
            } else if (id == 2) {
                AutoCompressorBlockEntity.this.setDisabledByRedstone(value == 1);
            }
        }

        public int getCount() {
            return 3;
        }
    };

    private NonNullList<ItemStack> currentBuffer = NonNullList.create();
    private CompressedRecipe currentRecipe = null;
    private float progress;
    private boolean isDisabledByRedstone;

    public AutoCompressorBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.autoCompressor.get(), pos, state);
    }

    public AutoCompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean shouldCompress(Multiset<CompressedRecipe> inputItems, CompressedRecipe compressedRecipe) {
        return inputItems.count(compressedRecipe) >= compressedRecipe.getCount();
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, AutoCompressorBlockEntity blockEntity) {
        blockEntity.serverTick();
    }

    public void serverTick() {
        int effectiveEnergy = getEffectiveEnergy();
        if (!isDisabledByRedstone() && overflowBuffer.isEmpty() && energyStorage.getEnergy() > effectiveEnergy) {
            if (currentRecipe == null) {
                inputItems.clear();
                for (int i = 0; i < inputSlots.getContainerSize(); i++) {
                    ItemStack slotStack = inputSlots.getItem(i);
                    if (!slotStack.isEmpty()) {
                        CompressedRecipe compressedRecipe = ExRegistries.getCompressedRecipeRegistry().getRecipe(slotStack);
                        if (compressedRecipe != null) {
                            inputItems.add(compressedRecipe, slotStack.getCount());
                        }
                    }
                }
                for (CompressedRecipe compressedRecipe : inputItems.elementSet()) {
                    Ingredient ingredient = compressedRecipe.getIngredient();
                    if (shouldCompress(inputItems, compressedRecipe)) {
                        int space = 0;
                        for (int i = 0; i < outputSlots.getContainerSize(); i++) {
                            ItemStack slotStack = outputSlots.getItem(i);
                            if (slotStack.isEmpty()) {
                                space = 64;
                            } else if (isItemEqualWildcard(slotStack, compressedRecipe.getResultStack())) {
                                space += slotStack.getMaxStackSize() - slotStack.getCount();
                            }
                            if (space >= compressedRecipe.getResultStack().getCount()) {
                                break;
                            }
                        }
                        if (space < compressedRecipe.getResultStack().getCount()) {
                            continue;
                        }
                        int count = compressedRecipe.getCount();
                        for (int i = 0; i < inputSlots.getContainerSize(); i++) {
                            ItemStack slotStack = inputSlots.getItem(i);
                            if (!slotStack.isEmpty() && ingredient.test(slotStack)) {
                                if (slotStack.getCount() >= count) {
                                    currentBuffer.add(slotStack.split(count));
                                    if (slotStack.isEmpty()) {
                                        inputSlots.setItem(i, ItemStack.EMPTY);
                                    }
                                    count = 0;
                                    break;
                                } else {
                                    currentBuffer.add(slotStack.copy());
                                    count -= slotStack.getCount();
                                    inputSlots.setItem(i, ItemStack.EMPTY);
                                }
                            }
                        }
                        if (count <= 0) {
                            currentRecipe = compressedRecipe;
                            progress = 0f;
                        }
                        break;
                    }
                }
            } else {
                energyStorage.drain(effectiveEnergy, false);
                progress = Math.min(1f, progress + getEffectiveSpeed());
                if (progress >= 1) {
                    if (!level.isClientSide) {
                        CompressedRecipe compressedRecipe = currentRecipe;
                        if (compressedRecipe != null) {
                            ItemStack resultStack = compressedRecipe.getResultStack().copy();
                            if (!addItemToOutput(resultStack)) {
                                overflowBuffer.add(resultStack);
                            }
                        }
                    }
                    currentBuffer.clear();
                    currentRecipe = null;
                    progress = 0f;
                }
            }
        } else if (!overflowBuffer.isEmpty()) {
            if (addItemToOutput(overflowBuffer.get(0))) {
                overflowBuffer.remove(0);
            }
        }
    }

    private boolean isItemEqualWildcard(ItemStack itemStack, ItemStack otherStack) {
        return ItemStack.isSameItemSameTags(itemStack, otherStack) &&
                (ItemStack.isSameItem(itemStack, otherStack) || itemStack.getItem() == otherStack.getItem());
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
                if (slotStack.getCount() + itemStack.getCount() <= slotStack.getMaxStackSize() && isItemEqualWildcard(slotStack, itemStack)) {
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
        return ExCompressumConfig.getActive().automation.autoCompressorEnergy;
    }

    public float getEffectiveSpeed() {
        return (float) ExCompressumConfig.getActive().automation.autoCompressorSpeed;
    }

    @Override
    public void load(CompoundTag tagCompound) {
        super.load(tagCompound);
        if (tagCompound.contains("CurrentRecipeResult")) {
            ItemStack itemStack = ItemStack.of(tagCompound.getCompound("CurrentRecipeResult"));
            if (!itemStack.isEmpty()) {
                currentRecipe = new CompressedRecipe(Ingredient.EMPTY, 0, itemStack);
            }
        }
        isDisabledByRedstone = tagCompound.getBoolean("IsDisabledByRedstone");
        progress = tagCompound.getFloat("Progress");
        backingContainer.deserialize(tagCompound.getCompound("ItemHandler"));
        energyStorage.deserialize(tagCompound.get("EnergyStorage"));
        overflowBuffer.clear();
        for (final var overflowItem : tagCompound.getList("OverflowBuffer", Tag.TAG_COMPOUND)) {
            overflowBuffer.add(ItemStack.of(((CompoundTag) overflowItem)));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (currentRecipe != null) {
            tag.put("CurrentRecipeResult", currentRecipe.getResultStack().save(new CompoundTag()));
        }
        tag.putBoolean("IsDisabledByRedstone", isDisabledByRedstone);
        tag.putFloat("Progress", progress);
        tag.put("ItemHandler", backingContainer.serialize());
        tag.put("EnergyStorage", energyStorage.serialize());
        final var overflowList = new ListTag();
        for (ItemStack itemStack : overflowBuffer) {
            overflowList.add(itemStack.save(new CompoundTag()));
        }
        tag.put("OverflowBuffer", overflowList);
    }

    @Override
    public void writeUpdateTag(CompoundTag tag) {
        saveAdditional(tag);
    }

    public boolean isProcessing() {
        return progress > 0f;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public float getEnergyPercentage() {
        return (float) energyStorage.getEnergy() / (float) energyStorage.getCapacity();
    }

    public NonNullList<ItemStack> getCurrentBuffer() {
        return currentBuffer; // not saved currently which can cause item loss if you break an Auto Compressor that was reload in between a single run (which is so unlikely to happen I won't bother)
    }

    public Container getBackingContainer() {
        return backingContainer;
    }

    @Override
    public Container getContainer() {
        return container;
    }

    @Override
    public Container getContainer(Direction side) {
        if (side == Direction.DOWN) {
            return outputSlots;
        }

        return BalmContainerProvider.super.getContainer(side);
    }

    @Override
    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public boolean isDisabledByRedstone() {
        return isDisabledByRedstone;
    }

    public void setDisabledByRedstone(boolean disabledByRedstone) {
        isDisabledByRedstone = disabledByRedstone;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.excompressum.auto_compressor");
    }

    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory inv, Player player) {
        return new AutoCompressorMenu(windowId, inv, this);
    }

    public ContainerData getContainerData() {
        return containerData;
    }
}
