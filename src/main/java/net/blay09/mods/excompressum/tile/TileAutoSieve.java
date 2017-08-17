package net.blay09.mods.excompressum.tile;

import cofh.redstoneflux.api.IEnergyReceiver;
import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;

@Optional.Interface(modid = "redstoneflux", iface = "cofh.redstoneflux.api.IEnergyReceiver")
public class TileAutoSieve extends TileAutoSieveBase implements IEnergyReceiver {

    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(32000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if(!simulate) {
                markDirty();
            }
            return super.receiveEnergy(maxReceive, simulate);
        }
    };

    @Override
    protected void writeToNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        super.writeToNBTSynced(tagCompound, isSync);
        NBTBase energyStorageNBT = CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
        if(energyStorageNBT != null) {
            tagCompound.setTag("EnergyStorage", energyStorageNBT);
        }
    }

    @Override
    protected void readFromNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        super.readFromNBTSynced(tagCompound, isSync);
        if(tagCompound.hasKey("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.getTag("EnergyStorage"));
        }
    }

    @Override
    public int getEnergyStored(@Nullable EnumFacing from) {
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
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return energyStorage;
    }

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getMaxEnergyStored(EnumFacing from) {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return true;
    }
}
