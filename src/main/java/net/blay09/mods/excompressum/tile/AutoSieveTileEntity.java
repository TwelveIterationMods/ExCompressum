package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

public class AutoSieveTileEntity extends AutoSieveTileEntityBase {

    private final EnergyStorageModifiable energyStorage = new EnergyStorageModifiable(32000) {
        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            if (!simulate) {
                markDirty();
            }
            return super.receiveEnergy(maxReceive, simulate);
        }
    };

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        super.writeToNBTSynced(tagCompound, isSync);
        INBT energyStorageNBT = CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
        if (energyStorageNBT != null) {
            tagCompound.put("EnergyStorage", energyStorageNBT);
        }
    }

    @Override
    protected void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        super.readFromNBTSynced(tagCompound, isSync);
        if (tagCompound.contains("EnergyStorage")) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tagCompound.get("EnergyStorage"));
        }
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!simulate) {
            isDirty = true;
        }
        return energyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            return (T) energyStorage;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
        return capability == CapabilityEnergy.ENERGY
                || super.hasCapability(capability, facing);
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return energyStorage;
    }

}
