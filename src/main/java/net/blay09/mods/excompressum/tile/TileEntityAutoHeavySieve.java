package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.data.SiftingResult;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public class TileEntityAutoHeavySieve extends TileEntityAutoSieve {

    @Override
    public boolean isRegistered(ItemStack itemStack) {
        return HeavySieveRegistry.isRegistered(itemStack);
    }

    @Override
    public Collection<SiftingResult> getSiftingOutput(ItemStack itemStack) {
        return HeavySieveRegistry.getSiftingOutput(itemStack);
    }

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.autoHeavySieveEnergy;
    }

    public float getEffectiveSpeed() {
        return ExCompressumConfig.autoHeavySieveSpeed * getSpeedBoost();
    }

}
