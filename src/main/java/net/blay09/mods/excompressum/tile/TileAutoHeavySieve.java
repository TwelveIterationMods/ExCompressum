package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public class TileAutoHeavySieve extends TileAutoSieve {

    @Override
    public boolean isSiftable(ItemStack itemStack) {
        return HeavySieveRegistry.isSiftable(itemStack);
    }

    @Override
    public boolean isSiftableWithMesh(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        return HeavySieveRegistry.isSiftableWithMesh(itemStack, sieveMesh);
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        return HeavySieveRegistry.rollSieveRewards(itemStack, sieveMesh, luck, rand);
    }

    @Override
    public int getEffectiveEnergy() {
        return ModConfig.automation.autoHeavySieveEnergy;
    }

    public float getEffectiveSpeed() {
        return ModConfig.automation.autoHeavySieveSpeed * getSpeedMultiplier();
    }

}
