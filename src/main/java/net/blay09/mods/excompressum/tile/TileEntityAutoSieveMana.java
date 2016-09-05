package net.blay09.mods.excompressum.tile;

import cpw.mods.fml.common.Optional;
import net.blay09.mods.excompressum.compat.botania.BotaniaAddon;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.api.mana.IManaReceiver;

@Optional.Interface(modid = "Botania", iface = "vazkii.botania.api.mana.IManaReceiver", striprefs = true)
public class TileEntityAutoSieveMana extends TileEntityAutoSieveBase implements IManaReceiver {

    private int manaStored;

    @Override
    public void setEnergyStored(int energyStored) {
        this.manaStored = Math.max(0, Math.min(getMaxEnergyStored(), energyStored));
    }

    @Override
    public int getEffectiveEnergy() {
        return BotaniaAddon.manaSieveCost;
    }

    @Override
    public int getMaxEnergyStored() {
        return BotaniaAddon.manaSieveCost * 1600;
    }

    @Override
    public int getEnergyStored() {
        return manaStored;
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setInteger("ManaStored", manaStored);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        manaStored = tagCompound.getInteger("ManaStored");
    }

    @Override
    public boolean isFull() {
        return manaStored >= getMaxEnergyStored();
    }

    @Override
    public void recieveMana(int mana) {
        manaStored += mana;
    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return !isFull();
    }

    @Override
    public int getCurrentMana() {
        return manaStored;
    }

}
