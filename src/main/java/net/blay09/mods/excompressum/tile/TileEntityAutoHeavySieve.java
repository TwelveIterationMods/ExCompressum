package net.blay09.mods.excompressum.tile;

import exnihilo.registries.helpers.SiftingResult;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
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

    public int getEffectiveEnergy() {
        return ExCompressum.autoHeavySieveEnergy;
    }

    public float getEffectiveSpeed() {
        return ExCompressum.autoHeavySieveSpeed;
    }

}
