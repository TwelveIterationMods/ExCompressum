package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.config.AutomationConfig;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public class TileAutoHeavySieve extends TileAutoSieve {

    @Override
    public boolean isSiftable(ItemStack itemStack) {
        return HeavySieveRegistry.isSiftable(itemStack);
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, int meshLevel, float luck, Random rand) {
        return HeavySieveRegistry.rollSieveRewards(itemStack, luck, rand);
    }

    @Override
    public int getEffectiveEnergy() {
        return AutomationConfig.autoHeavySieveEnergy;
    }

    public float getEffectiveSpeed() {
        return AutomationConfig.autoHeavySieveSpeed * getSpeedBoost();
    }

}
