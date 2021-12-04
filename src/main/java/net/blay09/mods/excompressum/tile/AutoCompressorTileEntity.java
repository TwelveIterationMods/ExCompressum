package net.blay09.mods.excompressum.tile;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.container.AutoCompressorContainer;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.blay09.mods.excompressum.utils.DefaultItemHandler;
import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.blay09.mods.excompressum.utils.ItemHandlerAutomation;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;

public class AutoCompressorTileEntity extends BaseTileEntity implements ITickableTileEntity, INamedContainerProvider {

    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(32000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!simulate) {
                markDirty();
            }
            return super.receiveEnergy(maxReceive, simulate);
        }
    };


    private final Multiset<CompressedRecipe> inputItems = HashMultiset.create();
    private final DefaultItemHandler itemHandler = new DefaultItemHandler(this, 24) {
        @Override
        public boolean isItemValid(int slot, ItemStack itemStack) {
            return slot >= 12 || ExRegistries.getCompressedRecipeRegistry().getRecipe(itemStack) != null;
        }
    };
    private final RangedWrapper inputSlots = new RangedWrapper(itemHandler, 0, 12);
    private final RangedWrapper outputSlots = new RangedWrapper(itemHandler, 12, 24);
    private final ItemHandlerAutomation itemHandlerAutomation = new ItemHandlerAutomation(itemHandler) {
        @Override
        public boolean canInsertItem(int slot, ItemStack itemStack) {
            return slot < 12 && ExRegistries.getCompressedRecipeRegistry().getRecipe(itemStack) != null;
        }

        @Override
        public boolean canExtractItem(int slot, int amount) {
            return slot >= 12;
        }
    };

    private final LazyOptional<EnergyStorage> energyStorageCap = LazyOptional.of(() -> energyStorage);
    private final LazyOptional<ItemHandlerAutomation> itemHandlerAutomationCap = LazyOptional.of(() -> itemHandlerAutomation);

    private NonNullList<ItemStack> currentBuffer = NonNullList.create();
    private CompressedRecipe currentRecipe = null;
    private float progress;
    private boolean isDisabledByRedstone;

    public AutoCompressorTileEntity() {
        this(ModTileEntities.autoCompressor);
    }

    public AutoCompressorTileEntity(TileEntityType<?> type) {
        super(type);
    }

    public boolean shouldCompress(Multiset<CompressedRecipe> inputItems, CompressedRecipe compressedRecipe) {
        return inputItems.count(compressedRecipe) >= compressedRecipe.getCount();
    }

    @Override
    public void tick() {
        int effectiveEnergy = getEffectiveEnergy();
        if (!world.isRemote && !isDisabledByRedstone() && energyStorage.getEnergyStored() > effectiveEnergy) {
            if (currentRecipe == null) {
                inputItems.clear();
                for (int i = 0; i < inputSlots.getSlots(); i++) {
                    ItemStack slotStack = inputSlots.getStackInSlot(i);
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
                        for (int i = 0; i < outputSlots.getSlots(); i++) {
                            ItemStack slotStack = outputSlots.getStackInSlot(i);
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
                        for (int i = 0; i < inputSlots.getSlots(); i++) {
                            ItemStack slotStack = inputSlots.getStackInSlot(i);
                            if (!slotStack.isEmpty() && ingredient.test(slotStack)) {
                                if (slotStack.getCount() >= count) {
                                    currentBuffer.add(slotStack.split(count));
                                    if (slotStack.isEmpty()) {
                                        inputSlots.setStackInSlot(i, ItemStack.EMPTY);
                                    }
                                    count = 0;
                                    break;
                                } else {
                                    currentBuffer.add(slotStack.copy());
                                    count -= slotStack.getCount();
                                    inputSlots.setStackInSlot(i, ItemStack.EMPTY);
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
                energyStorage.extractEnergy(effectiveEnergy, false);
                progress = Math.min(1f, progress + getEffectiveSpeed());
                if (progress >= 1) {
                    if (!world.isRemote) {
                        CompressedRecipe compressedRecipe = currentRecipe;
                        if (compressedRecipe != null) {
                            ItemStack resultStack = compressedRecipe.getResultStack().copy();
                            if (!addItemToOutput(resultStack)) {
                                ItemEntity entityItem = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, resultStack);
                                double motion = 0.05;
                                entityItem.setMotion(world.rand.nextGaussian() * motion, 0.2, world.rand.nextGaussian() * motion);
                                world.addEntity(entityItem);
                            }
                        }
                    }
                    currentBuffer.clear();
                    currentRecipe = null;
                    progress = 0f;
                }
            }
        }
    }

    private boolean isItemEqualWildcard(ItemStack itemStack, ItemStack otherStack) {
        return ItemStack.areItemStackTagsEqual(itemStack, otherStack) && (itemStack.isItemEqual(otherStack)
                || itemStack.getItem() == otherStack.getItem());
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
                if (slotStack.getCount() + itemStack.getCount() <= slotStack.getMaxStackSize() && isItemEqualWildcard(slotStack, itemStack)) {
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
        return ExCompressumConfig.COMMON.autoCompressorEnergy.get();
    }

    public float getEffectiveSpeed() {
        return ExCompressumConfig.COMMON.autoCompressorSpeed.get().floatValue();
    }

    @Override
    protected boolean hasUpdatePacket() {
        return false;
    }

    @Override
    public void read(BlockState state, CompoundNBT tagCompound) {
        super.read(state, tagCompound);
        if (tagCompound.contains("CurrentRecipeResult")) {
            ItemStack itemStack = ItemStack.read(tagCompound.getCompound("CurrentRecipeResult"));
            if (!itemStack.isEmpty()) {
                currentRecipe = new CompressedRecipe(Ingredient.EMPTY, 0, itemStack);
            }
        }
        isDisabledByRedstone = tagCompound.getBoolean("IsDisabledByRedstone");
    }

    @Override
    protected void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        progress = tagCompound.getFloat("Progress");
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        if (tagCompound.contains("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.get("EnergyStorage"));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT tagCompound) {
        if (currentRecipe != null) {
            tagCompound.put("CurrentRecipeResult", currentRecipe.getResultStack().write(new CompoundNBT()));
        }
        tagCompound.putBoolean("IsDisabledByRedstone", isDisabledByRedstone);
        return super.write(tagCompound);
    }

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        tagCompound.putFloat("Progress", progress);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        INBT energyStorageNBT = CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
        if (energyStorageNBT != null) {
            tagCompound.put("EnergyStorage", energyStorageNBT);
        }
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
        return (float) energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored();
    }

    public NonNullList<ItemStack> getCurrentBuffer() {
        return currentBuffer; // not saved currently which can cause item loss if you break an Auto Compressor that was reload in between a single run (which is so unlikely to happen I won't bother)
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

    @Override
    public void invalidateCaps() {
        energyStorageCap.invalidate();
        itemHandlerAutomationCap.invalidate();
        super.invalidateCaps();
    }

    public DefaultItemHandler getItemHandler() {
        return itemHandler;
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return energyStorage;
    }

    public boolean isDisabledByRedstone() {
        return isDisabledByRedstone;
    }

    public void setDisabledByRedstone(boolean disabledByRedstone) {
        isDisabledByRedstone = disabledByRedstone;
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.excompressum.auto_compressor");
    }

    @Override
    public Container createMenu(int windowId, PlayerInventory inv, PlayerEntity player) {
        return new AutoCompressorContainer(windowId, inv, this);
    }
}
