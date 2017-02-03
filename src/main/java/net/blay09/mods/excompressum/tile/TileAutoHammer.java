package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.config.ProcessingConfig;
import net.blay09.mods.excompressum.client.render.ParticleAutoHammer;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.utils.DefaultItemHandler;
import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.blay09.mods.excompressum.utils.ItemHandlerAutomation;
import net.blay09.mods.excompressum.utils.SubItemHandler;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public class TileAutoHammer extends TileEntityBase implements ITickable {

    private static final int UPDATE_INTERVAL = 20;

    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(32000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if(!simulate) {
                isDirty = true;
            }
            return super.receiveEnergy(maxReceive, simulate);
        }
    };
    private final DefaultItemHandler itemHandler = new DefaultItemHandler(this, 23) {
        @Override
        public boolean isItemValid(int slot, ItemStack itemStack) {
            if(slot == 0) {
                return isRegistered(itemStack);
            } else if(slot == 21 || slot == 22) {
                return isHammerUpgrade(itemStack);
            }
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            // Make sure the hammer slots are always synced.
            if(hammerSlots.isInside(slot)) {
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

    private ItemStack currentStack;

    private int ticksSinceUpdate;
    private boolean isDirty;
    private float progress;

    public float hammerAngle;

    @Override
    public void update() {
        int effectiveEnergy = getEffectiveEnergy();
        if (energyStorage.getEnergyStored() >= effectiveEnergy) {
            if (currentStack == null) {
                ItemStack inputStack = inputSlots.getStackInSlot(0);
                if (inputStack != null && isRegistered(inputStack)) {
                    boolean foundSpace = false;
                    for(int i = 0; i < outputSlots.getSlots(); i++) {
                        if(outputSlots.getStackInSlot(i) == null) {
                            foundSpace = true;
                        }
                    }
                    if(!foundSpace) {
                        return;
                    }
                    currentStack = inputStack.splitStack(1);
                    if (inputStack.stackSize == 0) {
                        inputSlots.setStackInSlot(0, null);
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
                    if (!worldObj.isRemote) {
                        if(worldObj.rand.nextFloat() <= ProcessingConfig.autoHammerDecay) {
                            ItemStack firstHammer = hammerSlots.getStackInSlot(0);
                            if (firstHammer != null) {
                                if(firstHammer.attemptDamageItem(1, worldObj.rand)) {
                                    hammerSlots.setStackInSlot(0, null);
                                }
                            }
                            ItemStack secondHammer = hammerSlots.getStackInSlot(1);
                            if (secondHammer != null) {
                                if(secondHammer.attemptDamageItem(1, worldObj.rand)) {
                                    hammerSlots.setStackInSlot(1, null);
                                }
                            }
                        }
                        Collection<ItemStack> rewards = rollHammerRewards(currentStack, getMiningLevel(), getEffectiveLuck(), worldObj.rand);
                        for (ItemStack itemStack : rewards) {
                            if (!addItemToOutput(itemStack)) {
                                EntityItem entityItem = new EntityItem(worldObj, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, itemStack);
                                double motion = 0.05;
                                entityItem.motionX = worldObj.rand.nextGaussian() * motion;
                                entityItem.motionY = 0.2;
                                entityItem.motionZ = worldObj.rand.nextGaussian() * motion;
                                worldObj.spawnEntityInWorld(entityItem);
                            }
                        }
                    } else {
                        spawnCrushParticles();
                    }
                    progress = 0f;
                    currentStack = null;
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

    private boolean addItemToOutput(ItemStack itemStack) {
        int firstEmptySlot = -1;
        for (int i = 0; i < outputSlots.getSlots(); i++) {
            ItemStack slotStack = outputSlots.getStackInSlot(i);
            if (slotStack == null) {
                if(firstEmptySlot == -1){
                    firstEmptySlot = i;
                }
            } else {
                if (slotStack.stackSize + itemStack.stackSize <= slotStack.getMaxStackSize() && slotStack.isItemEqual(itemStack) && ItemStack.areItemStackTagsEqual(slotStack, itemStack)) {
                    slotStack.stackSize += itemStack.stackSize;
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
        return ProcessingConfig.autoHammerEnergy;
    }

    public float getSpeedMultiplier() {
        final float HAMMER_BOOST = 0.5f;
        final float EFFICIENCY_BOOST = 0.5f;
        float boost = 1f;
        ItemStack firstHammer = hammerSlots.getStackInSlot(0);
        if(firstHammer != null && isHammerUpgrade(firstHammer)) {
            boost += HAMMER_BOOST;
            boost += EFFICIENCY_BOOST * EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, firstHammer);
        }
        ItemStack secondHammer = hammerSlots.getStackInSlot(1);
        if(secondHammer != null && isHammerUpgrade(secondHammer)) {
            boost += HAMMER_BOOST;
            boost += EFFICIENCY_BOOST * EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, secondHammer);
        }
        return boost;
    }

    public float getEffectiveSpeed() {
        return ProcessingConfig.autoHammerSpeed * getSpeedMultiplier();
    }

    public float getEffectiveLuck() {
        float luck = 0f;
        ItemStack firstHammer = hammerSlots.getStackInSlot(0);
        if(firstHammer != null && isHammerUpgrade(firstHammer)) {
            luck += EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, firstHammer);
        }
        ItemStack secondHammer = hammerSlots.getStackInSlot(1);
        if(secondHammer != null && isHammerUpgrade(secondHammer)) {
            luck += EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, secondHammer);
        }
        return luck;
    }

    @Override
    protected boolean hasUpdatePacket() {
        return true;
    }

    @Override
    protected void readFromNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        currentStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("CurrentStack"));
        progress = tagCompound.getFloat("Progress");
        if(tagCompound.hasKey("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.getTag("EnergyStorage"));
        }
        if(isSync) {
            hammerSlots.setStackInSlot(0, ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("FirstHammer")));
            hammerSlots.setStackInSlot(1, ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("SecondHammer")));
        } else {
            itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
        }
    }

    @Override
    protected void writeToNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        tagCompound.setTag("EnergyStorage", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
        if (currentStack != null) {
            tagCompound.setTag("CurrentStack", currentStack.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setFloat("Progress", progress);
        if(isSync) {
            ItemStack firstHammer = hammerSlots.getStackInSlot(0);
            if(firstHammer != null) {
                tagCompound.setTag("FirstHammer", firstHammer.writeToNBT(new NBTTagCompound()));
            }
            ItemStack secondHammer = hammerSlots.getStackInSlot(1);
            if(secondHammer != null) {
                tagCompound.setTag("SecondHammer", secondHammer.writeToNBT(new NBTTagCompound()));
            }
        } else {
            tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnCrushParticles() {
        if(!ExCompressumConfig.disableParticles) {
            IBlockState currentBlock = getCurrentBlock();
            if (currentBlock != null) {
                for (int i = 0; i < 10; i++) {
                    Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleAutoHammer(worldObj, pos, pos.getX() + 0.7f, pos.getY() + 0.3f, pos.getZ() + 0.5f, (-worldObj.rand.nextDouble() + 0.2f) / 9, 0.2f, (worldObj.rand.nextDouble() - 0.5) / 9, currentBlock));
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
        return (float) energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored();
    }

    @Nullable
    public ItemStack getCurrentStack() {
        return currentStack;
    }

    @Nullable
    public IBlockState getCurrentBlock() {
        if(currentStack == null) {
            return null;
        }
        Block block = Block.getBlockFromItem(currentStack.getItem());
        //noinspection ConstantConditions /// NO IT'S NOT. getBlockFromItem not marked @Nullable
        if(block != null) {
            //noinspection deprecation /// mojang pls
            return block.getStateFromMeta(currentStack.getMetadata());
        }
        return null;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY
                || capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
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

    @Nullable
    public ItemStack getUpgradeStack(int i) {
        return hammerSlots.getStackInSlot(i);
    }

    public boolean isHammerUpgrade(ItemStack itemStack) {
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
        return currentStack != null && energyStorage.getEnergyStored() >= getEffectiveEnergy();
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return energyStorage;
    }
}
