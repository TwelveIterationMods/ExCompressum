package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.utils.EnergyStorageModifiable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;

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

    private final LazyOptional<EnergyStorage> energyStorageCap = LazyOptional.of(() -> energyStorage);

    public AutoSieveTileEntity() {
        super(ModTileEntities.autoSieve);
    }

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        super.writeToNBTSynced(tagCompound, isSync);
        INBT energyStorageNBT = CapabilityEnergy.ENERGY.writeNBT(energyStorage, null);
        if (energyStorageNBT != null) {
            tagCompound.put("EnergyStorage", energyStorageNBT);
        }
    }

    @Override
    protected int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public void setEnergyStored(int energy) {
        energyStorage.setEnergyStored(energy);
    }

    @Override
    protected int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
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
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return (LazyOptional<T>) energyStorageCap;
        }
        return super.getCapability(cap, side);
    }

    public EnergyStorageModifiable getEnergyStorage() {
        return energyStorage;
    }

}
