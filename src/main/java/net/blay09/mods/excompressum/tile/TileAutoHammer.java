package net.blay09.mods.excompressum.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.blay09.mods.excompressum.DefaultItemHandler;
import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.ItemHandlerAutomation;
import net.blay09.mods.excompressum.client.render.ParticleAutoHammer;
import net.blay09.mods.excompressum.handler.VanillaPacketHandler;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public class TileAutoHammer extends TileEntityBase implements ITickable, IEnergyReceiver {

    private static final int UPDATE_INTERVAL = 20;

    private final EnergyStorage storage = new EnergyStorage(32000);
    private DefaultItemHandler itemHandler = new DefaultItemHandler(this, 23) {
        @Override
        public boolean isItemValid(int slot, ItemStack itemStack) {
            if(slot == 0) {
                return isRegistered(itemStack);
            } else if(slot == 21 || slot == 22) {
                return isHammerUpgrade(itemStack);
            }
            return true;
        }
    };
    private RangedWrapper itemHandlerInput = new RangedWrapper(itemHandler, 0, 1);
    private RangedWrapper itemHandlerOutput = new RangedWrapper(itemHandler, 1, 21);
    private RangedWrapper itemHandlerUpgrades = new RangedWrapper(itemHandler, 21, 23);
    private ItemHandlerAutomation itemHandlerAutomation = new ItemHandlerAutomation(itemHandler) {
        @Override
        public boolean canExtractItem(int slot, int amount) {
            return slot >= 1 && slot <= 20;
        }
    };
    private ItemStack currentStack;

    private int ticksSinceUpdate;
    private boolean isDirty;
    private float progress;

    @Override
    public void update() {
        ticksSinceUpdate++;
        if (ticksSinceUpdate > UPDATE_INTERVAL) {
            if (isDirty) {
                VanillaPacketHandler.sendTileEntityUpdate(this);
                isDirty = false;
            }
            ticksSinceUpdate = 0;
        }
        int effectiveEnergy = getEffectiveEnergy();
        if (storage.getEnergyStored() >= effectiveEnergy) {
            if (currentStack == null) {
                ItemStack inputStack = itemHandlerInput.getStackInSlot(0);
                if (inputStack != null && isRegistered(inputStack)) {
                    boolean foundSpace = false;
                    for(int i = 0; i < itemHandlerOutput.getSlots(); i++) {
                        if(itemHandlerOutput.getStackInSlot(i) == null) {
                            foundSpace = true;
                        }
                    }
                    if(!foundSpace) {
                        return;
                    }
                    currentStack = inputStack.splitStack(1);
                    if (inputStack.stackSize == 0) {
                        itemHandlerInput.setStackInSlot(0, null);
                    }
                    storage.extractEnergy(effectiveEnergy, false);
                    VanillaPacketHandler.sendTileEntityUpdate(this);
                    progress = 0f;
                }
            } else {
                storage.extractEnergy(effectiveEnergy, false);
                progress += getEffectiveSpeed();
                isDirty = true;
                if (progress >= 1) {
                    if(worldObj.rand.nextFloat() <= ExCompressumConfig.autoHammerDecay) {
                        ItemStack firstHammer = itemHandlerUpgrades.getStackInSlot(0);
                        if (firstHammer != null) {
                            if(firstHammer.attemptDamageItem(1, worldObj.rand)) {
                                itemHandlerUpgrades.setStackInSlot(0, null);
                            }
                        }
                        ItemStack secondHammer = itemHandlerUpgrades.getStackInSlot(1);
                        if (secondHammer != null) {
                            if(secondHammer.attemptDamageItem(1, worldObj.rand)) {
                                itemHandlerUpgrades.setStackInSlot(1, null);
                            }
                        }
                    }
                    if (!worldObj.isRemote) {
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
    }

    private boolean addItemToOutput(ItemStack itemStack) {
        int firstEmptySlot = -1;
        for (int i = 0; i < itemHandlerOutput.getSlots(); i++) {
            ItemStack slotStack = itemHandlerOutput.getStackInSlot(i);
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
            itemHandlerOutput.setStackInSlot(firstEmptySlot, itemStack);
            return true;
        }
        return false;
    }

    public int getEffectiveEnergy() {
        return ExCompressumConfig.autoHammerEnergy;
    }

    public float getSpeedBoost() {
        float boost = 1f;
        ItemStack firstHammer = itemHandlerUpgrades.getStackInSlot(0);
        if(firstHammer != null && isHammerUpgrade(firstHammer)) {
            boost += 1f;
        }
        ItemStack secondHammer = itemHandlerUpgrades.getStackInSlot(1);
        if(secondHammer != null && isHammerUpgrade(secondHammer)) {
            boost += 1f;
        }
        return boost;
    }

    public float getEffectiveSpeed() {
        return ExCompressumConfig.autoHammerSpeed * getSpeedBoost();
    } // TODO should probably take hammer enchantments into account

    public float getEffectiveLuck() {
        return 0f; // TODO should probably take hammer enchantments into account
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        readFromNBTSynced(tagCompound);
        storage.readFromNBT(tagCompound);
        itemHandler.deserializeNBT(tagCompound.getCompoundTag("ItemHandler"));
    }

    private void readFromNBTSynced(NBTTagCompound tagCompound) {
        currentStack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag("CurrentStack"));
        progress = tagCompound.getFloat("Progress");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        writeToNBTSynced(tagCompound);
        storage.writeToNBT(tagCompound);
        tagCompound.setTag("ItemHandler", itemHandler.serializeNBT());
        return tagCompound;
    }

    private void writeToNBTSynced(NBTTagCompound tagCompound) {
        if (currentStack != null) {
            tagCompound.setTag("CurrentStack", currentStack.writeToNBT(new NBTTagCompound()));
        }
        tagCompound.setFloat("Progress", progress);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        writeToNBTSynced(tagCompound);
        return new SPacketUpdateTileEntity(pos, getBlockMetadata(), tagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBTSynced(pkt.getNbtCompound());
    }

    @SideOnly(Side.CLIENT)
    private void spawnCrushParticles() {
        IBlockState currentBlock = getCurrentBlock();
        if (currentBlock != null) {
            for (int i = 0; i < 10; i++) {
                Minecraft.getMinecraft().effectRenderer.addEffect(new ParticleAutoHammer(worldObj, pos.getX() + 0.5, pos.getY() + 0.3125, pos.getZ() + 0.5, (worldObj.rand.nextDouble() / 2) - 0.25, 0, (worldObj.rand.nextDouble() / 2) - 0.25, currentBlock));
            }
        }
    }

    @Override
    public int receiveEnergy(EnumFacing side, int maxReceive, boolean simulate) {
        if(!simulate) {
            isDirty = true;
        }
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(@Nullable EnumFacing side) {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing side) {
        return storage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing side) {
        return true;
    }

    public void setEnergyStored(int energyStored) {
        storage.setEnergyStored(energyStored);
    }

    public boolean isProcessing() {
        return progress > 0f;
    }

    public float getProgress() {
        return progress;
    }

    public float getEnergyPercentage() {
        return (float) storage.getEnergyStored() / (float) storage.getMaxEnergyStored();
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
            || super.hasCapability(capability, facing);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) itemHandlerAutomation;
        }
        return super.getCapability(capability, facing);
    }

    public DefaultItemHandler getItemHandler() {
        return itemHandler;
    }

    @Nullable
    public ItemStack getUpgradeStack(int i) {
        return itemHandlerUpgrades.getStackInSlot(i);
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
}
