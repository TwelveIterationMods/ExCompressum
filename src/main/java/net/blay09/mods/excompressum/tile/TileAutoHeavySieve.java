package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.sieve.HeavySieveRegistry;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public class TileAutoHeavySieve extends TileAutoSieve {

    @Override
    public boolean isRegistered(ItemStack itemStack) {
        return HeavySieveRegistry.isSiftable(itemStack);
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, int meshLevel, float luck, Random rand) {
        return HeavySieveRegistry.rollSieveRewards(itemStack, luck, rand);
    }

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.autoHeavySieveEnergy;
    }

    public float getEffectiveSpeed() {
        return ExCompressumConfig.autoHeavySieveSpeed * getSpeedBoost();
    }

}
