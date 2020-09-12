package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Random;

public class AutoHeavySieveTileEntity extends AutoSieveTileEntity {

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
        return ExCompressumConfig.automation.autoHeavySieveEnergy;
    }

    public float getEffectiveSpeed() {
        return ExCompressumConfig.automation.autoHeavySieveSpeed * getSpeedMultiplier();
    }

}
