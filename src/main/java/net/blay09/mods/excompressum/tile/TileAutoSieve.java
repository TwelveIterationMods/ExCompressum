package net.blay09.mods.excompressum.tile;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

public class TileAutoSieve extends TileEntityAutoSieveBase implements IEnergyReceiver {

    private final EnergyStorage storage = new EnergyStorage(32000);

    @Override
    protected void writeToNBTSynced(NBTTagCompound tagCompound) {
        super.writeToNBTSynced(tagCompound);
        storage.writeToNBT(tagCompound);
    }

    @Override
    protected void readFromNBTSynced(NBTTagCompound tagCompound) {
        super.readFromNBTSynced(tagCompound);
        storage.readFromNBT(tagCompound);
    }

    @Override
    public int getEnergyStored() {
        return storage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return storage.getMaxEnergyStored();
    }

    @Override
    public void setEnergyStored(int energyStored) {
        storage.setEnergyStored(energyStored);
    }

    @Override
    public int receiveEnergy(EnumFacing side, int maxReceive, boolean simulate) {
        if(!simulate) {
            isDirty = true;
        }
        return storage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int getEnergyStored(EnumFacing side) {
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

}
