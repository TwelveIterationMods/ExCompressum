package net.blay09.mods.excompressum.tile;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.utils.DefaultItemHandler;
import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.blay09.mods.excompressum.utils.ItemHandlerAutomation;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;

public class AutoCompressorTileEntity extends BaseTileEntity implements ITickable {

    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(32000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if(!simulate) {
                markDirty();
            }
            return super.receiveEnergy(maxReceive, simulate);
        }
    };

    private final Multiset<CompressedRecipe> inputItems = HashMultiset.create();
    private final DefaultItemHandler itemHandler = new DefaultItemHandler(this, 24) {
        @Override
        public boolean isItemValid(int slot, ItemStack itemStack) {
            return slot >= 12 || CompressedRecipeRegistry.getRecipe(itemStack) != null;
        }
    };
    private final RangedWrapper inputSlots = new RangedWrapper(itemHandler, 0, 12);
    private final RangedWrapper outputSlots = new RangedWrapper(itemHandler, 12, 24);
    private final ItemHandlerAutomation itemHandlerAutomation = new ItemHandlerAutomation(itemHandler) {
        @Override
        public boolean canInsertItem(int slot, ItemStack itemStack) {
            return slot < 12 && CompressedRecipeRegistry.getRecipe(itemStack) != null;
        }

        @Override
        public boolean canExtractItem(int slot, int amount) {
            return slot >= 12;
        }
    };

    private NonNullList<ItemStack> currentBuffer = NonNullList.create();
    private CompressedRecipe currentRecipe = null;
    private float progress;
    private boolean isDisabledByRedstone;

    public boolean shouldCompress(Multiset<CompressedRecipe> inputItems, CompressedRecipe compressedRecipe) {
        return inputItems.count(compressedRecipe) >= compressedRecipe.getCount();
    }

    @Override
    public void update() {
        int effectiveEnergy = getEffectiveEnergy();
        if (!world.isRemote && !isDisabledByRedstone() && energyStorage.getEnergyStored() > effectiveEnergy) {
            if (currentRecipe == null) {
                inputItems.clear();
                for (int i = 0; i < inputSlots.getSlots(); i++) {
                    ItemStack slotStack = inputSlots.getStackInSlot(i);
                    if (!slotStack.isEmpty()) {
                        CompressedRecipe compressedRecipe = CompressedRecipeRegistry.getRecipe(slotStack);
                        if (compressedRecipe != null) {
                            inputItems.add(compressedRecipe, slotStack.getCount());
                        }
                    }
                }
                for (CompressedRecipe compressedRecipe : inputItems.elementSet()) {
                    Ingredient ingredient = compressedRecipe.getIngredient();
                    if (shouldCompress(inputItems, compressedRecipe)) {
                        int space = 0;
                        for(int i = 0; i < outputSlots.getSlots(); i++) {
                            ItemStack slotStack = outputSlots.getStackInSlot(i);
                            if(slotStack.isEmpty()) {
                                space = 64;
                            } else if(isItemEqualWildcard(slotStack, compressedRecipe.getResultStack())) {
                                space += slotStack.getMaxStackSize() - slotStack.getCount();
                            }
                            if(space >= compressedRecipe.getResultStack().getCount()) {
                                break;
                            }
                        }
                        if(space < compressedRecipe.getResultStack().getCount()) {
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
                || itemStack.getItem() == otherStack.getItem() && (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE
                || otherStack.getItemDamage() == OreDictionary.WILDCARD_VALUE));
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
        return ModConfig.automation.autoCompressorEnergy;
    }

    public float getEffectiveSpeed() {
        return ModConfig.automation.autoCompressorSpeed;
    }

    @Override
    protected boolean hasUpdatePacket() {
        return false;
    }

    @Override
    public void readFromNBT(CompoundNBT tagCompound) {
        super.readFromNBT(tagCompound);
        if(tagCompound.contains("CurrentRecipeResult")) {
            ItemStack itemStack = new ItemStack(tagCompound.getCompound("CurrentRecipeResult"));
            if(!itemStack.isEmpty()) {
                currentRecipe = new CompressedRecipe(Ingredient.EMPTY, 0, itemStack);
            }
        }
        isDisabledByRedstone = tagCompound.getBoolean("IsDisabledByRedstone");
    }

    @Override
    protected void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        progress = tagCompound.getFloat("Progress");
        itemHandler.deserializeNBT(tagCompound.getCompound("ItemHandler"));
        if(tagCompound.contains("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.contains("EnergyStorage"));
        }
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT tagCompound) {
        if(currentRecipe != null) {
            tagCompound.put("CurrentRecipeResult", currentRecipe.getResultStack().writeToNBT(new CompoundNBT()));
        }
        tagCompound.putBoolean("IsDisabledByRedstone", isDisabledByRedstone);
        return super.writeToNBT(tagCompound);
    }

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        tagCompound.putFloat("Progress", progress);
        tagCompound.put("ItemHandler", itemHandler.serializeNBT());
        INBT energyStorageNBT = CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
        if(energyStorageNBT != null) {
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
    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandlerAutomation;
        }
        if(capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
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
}
