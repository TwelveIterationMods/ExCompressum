package net.blay09.mods.excompressum.tile;

import cofh.api.energy.IEnergyReceiver;
import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

public class TileAutoSieve extends TileAutoSieveBase implements IEnergyReceiver {

    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(32000);

    @Override
    protected void writeToNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        super.writeToNBTSynced(tagCompound, isSync);
        tagCompound.setTag("EnergyStorage", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
    }

    @Override
    protected void readFromNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        super.readFromNBTSynced(tagCompound, isSync);
        if(tagCompound.hasKey("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.getTag("EnergyStorage"));
        }
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public void setEnergyStored(int energy) {
        energyStorage.setEnergyStored(energy);
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if(!simulate) {
            isDirty = true;
        }
        return energyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int receiveEnergy(EnumFacing side, int maxReceive, boolean simulate) {
        if(!simulate) {
            isDirty = true;
        }
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(EnumFacing side) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(EnumFacing side) {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing side) {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return energyStorage;
    }
}
