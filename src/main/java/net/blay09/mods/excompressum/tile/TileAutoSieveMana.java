package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.botania.BotaniaAddon;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Optional;
import vazkii.botania.api.mana.IManaReceiver;

@Optional.Interface(modid = Compat.BOTANIA, iface = "vazkii.botania.api.mana.IManaReceiver", striprefs = true)
public class TileAutoSieveMana extends TileAutoSieveBase implements IManaReceiver {

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
    protected void writeToNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        super.writeToNBTSynced(tagCompound, isSync);
        if(!isSync) {
            tagCompound.setInteger("ManaStored", manaStored);
        }
    }

    @Override
    protected void readFromNBTSynced(NBTTagCompound tagCompound, boolean isSync) {
        super.readFromNBTSynced(tagCompound, isSync);
        if(!isSync) {
            manaStored = tagCompound.getInteger("ManaStored");
        }
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
