package net.blay09.mods.excompressum.block.entity;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.blay09.mods.balm.platform.energy.BalmEnergyStorageProvider;
import net.blay09.mods.balm.platform.energy.DefaultEnergyStorage;
import net.blay09.mods.balm.platform.energy.EnergyStorage;
import net.blay09.mods.balm.world.*;
import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityUtils;
import net.blay09.mods.excompressum.component.ModComponents;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.menu.AutoCompressorMenu;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoCompressorBlockEntity extends AbstractBaseBlockEntity implements BalmMenuProvider<BlockPos>, BalmEnergyStorageProvider, BalmContainerProvider {

    private final DefaultEnergyStorage energyStorage = new DefaultEnergyStorage(32000) {
        @Override
        public int fill(int maxReceive, boolean simulate) {
            if (!simulate) {
                setChanged();
            }
            return super.fill(maxReceive, simulate);
        }
    };


    private final Multiset<CompressedRecipe> inputItems = HashMultiset.create();
    private final DefaultContainer backingContainer = new DefaultContainer(24);
    private final SubContainer inputSlots = new SubContainer(backingContainer, 0, 12);
    private final SubContainer outputSlots = new SubContainer(backingContainer, 12, 24);
    private final Container container = new DelegateContainer(backingContainer) {
        @Override
        public boolean canPlaceItem(int slot, ItemStack itemStack) {
            return inputSlots.containsOuterSlot(slot) && ExRegistries.getCompressedRecipeRegistry().getRecipe(itemStack) != null;
        }

        @Override
        public int[] getSlotsForFace(Direction direction) {
            if (direction == Direction.DOWN) {
                return outputSlots.getOuterSlotsForFace(direction);
            }
            return inputSlots.getOuterSlotsForFace(direction);
        }

        @Override
        public boolean canTakeItemThroughFace(int slot, ItemStack itemStack, Direction direction) {
            return outputSlots.containsOuterSlot(slot);
        }
    };

    private final List<ItemStack> overflowBuffer = new ArrayList<>();

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

    private final NonNullList<ItemStack> currentBuffer = NonNullList.create();
    private @Nullable CompressedRecipe currentRecipe;
    private float progress;
    private boolean isDisabledByRedstone;

    public AutoCompressorBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.autoCompressor.value(), pos, state);
    }

    public AutoCompressorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public boolean shouldCompress(Multiset<CompressedRecipe> inputItems, CompressedRecipe compressedRecipe) {
        return inputItems.count(compressedRecipe) >= compressedRecipe.count();
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
                    Ingredient ingredient = compressedRecipe.ingredient();
                    if (shouldCompress(inputItems, compressedRecipe)) {
                        int space = 0;
                        for (int i = 0; i < outputSlots.getContainerSize(); i++) {
                            ItemStack slotStack = outputSlots.getItem(i);
                            if (slotStack.isEmpty()) {
                                space = 64;
                            } else if (isItemEqualWildcard(slotStack, compressedRecipe.resultStack())) {
                                space += slotStack.getMaxStackSize() - slotStack.getCount();
                            }
                            if (space >= compressedRecipe.resultStack().getCount()) {
                                break;
                            }
                        }
                        if (space < compressedRecipe.resultStack().getCount()) {
                            continue;
                        }
                        int count = compressedRecipe.count();
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
                    if (!level.isClientSide()) {
                        CompressedRecipe compressedRecipe = currentRecipe;
                        if (compressedRecipe != null) {
                            ItemStack resultStack = compressedRecipe.resultStack().copy();
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
            if (addItemToOutput(overflowBuffer.getFirst())) {
                overflowBuffer.removeFirst();
            }
        }
    }

    private boolean isItemEqualWildcard(ItemStack itemStack, ItemStack otherStack) {
        return ItemStack.isSameItemSameComponents(itemStack, otherStack) &&
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
    public void loadAdditional(ValueInput input) {
        currentRecipe = input.getString("CurrentRecipe")
                .map(Identifier::parse)
                .map(ExRegistries.getCompressedRecipeRegistry()::getRecipeById)
                .orElse(currentRecipe);
        isDisabledByRedstone = input.getBooleanOr("IsDisabledByRedstone", false);
        progress = input.getFloatOr("Progress", 0);
        input.child("ItemHandler").ifPresent(it -> ContainerHelper.loadAllItems(it, backingContainer.getItems()));
        input.child("EnergyStorage").ifPresent(energyStorage::deserialize);
        overflowBuffer.clear();
        input.list("OverflowBuffer", ItemStack.CODEC).ifPresent(overflowItems -> {
            for (final var overflowItem : overflowItems) {
                overflowBuffer.add(overflowItem);
            }
        });
    }

    @Override
    public void saveAdditional(ValueOutput output) {
        if (currentRecipe != null) {
            output.putString("CurrentRecipe", currentRecipe.id().toString());
        }
        output.putBoolean("IsDisabledByRedstone", isDisabledByRedstone);
        output.putFloat("Progress", progress);
        ContainerHelper.saveAllItems(output.child("ItemHandler"), backingContainer.getItems());
        energyStorage.serialize(output.child("EnergyStorage"));
        final var overflowList = output.list("OverflowBuffer", ItemStack.CODEC);
        for (ItemStack itemStack : overflowBuffer) {
            overflowList.add(itemStack);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return BalmBlockEntityUtils.createUpdateTag(registries, this::saveAdditional);
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
    public @Nullable Container getContainer(Direction side) {
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

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder builder) {
        builder.set(ModComponents.energy.value(), energyStorage.getEnergy());
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter input) {
        final var energyComponent = input.get(ModComponents.energy.value());
        if (energyComponent != null) {
            energyStorage.setEnergy(energyComponent);
        }
    }

    @Override
    public BlockPos getScreenOpeningData(ServerPlayer serverPlayer) {
        return worldPosition;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, BlockPos> getScreenStreamCodec() {
        return BlockPos.STREAM_CODEC.cast();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        if (level != null) {
            for (final var currentStack : getCurrentBuffer()) {
                if (!currentStack.isEmpty()) {
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), currentStack));
                }
            }
        }
    }
}
