package net.blay09.mods.excompressum.tile;

import cofh.redstoneflux.api.IEnergyReceiver;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.utils.DefaultItemHandler;
import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.blay09.mods.excompressum.utils.ItemHandlerAutomation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;

@Optional.Interface(modid = "redstoneflux", iface = "cofh.redstoneflux.api.IEnergyReceiver")
public class TileAutoCompressor extends TileEntityBase implements ITickable, IEnergyReceiver {

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
        if (!world.isRemote && !isDisabledByRedstone() && getEnergyStored(null) > effectiveEnergy) {
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
                            if (!slotStack.isEmpty() && ingredient.apply(slotStack)) {
                                if (slotStack.getCount() >= count) {
                                    currentBuffer.add(slotStack.splitStack(count));
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
                                EntityItem entityItem = new EntityItem(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, resultStack);
                                double motion = 0.05;
                                entityItem.motionX = world.rand.nextGaussian() * motion;
                                entityItem.motionY = 0.2;
                                entityItem.motionZ = world.rand.nextGaussian() * motion;
                                world.spawnEntity(entityItem);
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
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        if(tagCompound.hasKey("CurrentRecipeResult")) {
            ItemStack itemStack = new ItemStack(tagCompound.getCompoundTag("CurrentRecipeResult"));
            if(!itemStack.isEmpty()) {
                currentRecipe = new CompressedRecipe(Ingredient.EMPTY, 0, itemStack);
            }
        }
        isDisabledByRedstone = tagCompound.getBoolean("IsDisabledByRedstone");
    }

    @Override
    protected void readFromNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        progress = tagCompound.getFloat("Progress");
        itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
        if(tagCompound.hasKey("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.getTag("EnergyStorage"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        if(currentRecipe != null) {
            tagCompound.setTag("CurrentRecipeResult", currentRecipe.getResultStack().writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setBoolean("IsDisabledByRedstone", isDisabledByRedstone);
        return super.writeToNBT(tagCompound);
    }

    @Override
    protected void writeToNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        tagCompound.setFloat("Progress", progress);
        tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
        NBTBase energyStorageNBT = CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
        if(energyStorageNBT != null) {
            tagCompound.setTag("EnergyStorage", energyStorageNBT);
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
        return (float) getEnergyStored(null) / (float) getMaxEnergyStored(null);
    }

    public NonNullList<ItemStack> getCurrentBuffer() {
        return currentBuffer; // not saved currently which can cause item loss if you break an Auto Compressor that was reload in between a single run (which is so unlikely to happen I won't bother)
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
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

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(@Nullable EnumFacing from) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(@Nullable EnumFacing from) {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }

    public boolean isDisabledByRedstone() {
        return isDisabledByRedstone;
    }

    public void setDisabledByRedstone(boolean disabledByRedstone) {
        isDisabledByRedstone = disabledByRedstone;
    }
}
