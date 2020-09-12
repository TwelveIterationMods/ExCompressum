package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.minecraft.nbt.CompoundNBT;

public class ManaSieveTileEntity extends AutoSieveTileEntityBase {

    private int manaStored;

    public ManaSieveTileEntity() {
        super(ModTileEntities.manaSieve);
    }

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.COMMON.manaSieveCost.get();
    }

    @Override
    public int getMaxEnergyStored() {
        return ExCompressumConfig.COMMON.manaSieveCost.get() * 1600;
    }

    @Override
    public int getEnergyStored() {
        return manaStored;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int manaExtracted = Math.min(manaStored, maxExtract);
        if (!simulate) {
            manaStored -= manaExtracted;
        }
        return manaExtracted;
    }

    @Override
    public void setEnergyStored(int energy) {
        this.manaStored = energy;
    }

    @Override
    protected void writeToNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        super.writeToNBTSynced(tagCompound, isSync);
        if (!isSync) {
            tagCompound.putInt("ManaStored", manaStored);
        }
    }

    @Override
    protected void readFromNBTSynced(CompoundNBT tagCompound, boolean isSync) {
        super.readFromNBTSynced(tagCompound, isSync);
        if (!isSync) {
            manaStored = tagCompound.getInt("ManaStored");
        }
    }

}
