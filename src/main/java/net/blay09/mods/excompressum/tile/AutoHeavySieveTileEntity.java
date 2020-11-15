package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySiftable;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class AutoHeavySieveTileEntity extends AutoSieveTileEntity {

    public AutoHeavySieveTileEntity() {
        super(ModTileEntities.autoHeavySieve);
    }

    @Override
    public boolean isSiftable(ItemStack itemStack) {
        return ExRegistries.getHeavySieveRegistry().isSiftable(itemStack);
    }

    @Override
    public boolean isSiftableWithMesh(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        return ExRegistries.getHeavySieveRegistry().isSiftable(itemStack/*, sieveMesh*/); // TODO revisit this
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        HeavySiftable siftable = ExRegistries.getHeavySieveRegistry().getSiftable(itemStack);
        if (siftable != null) {
            LootContext lootContext = HeavySieveRegistry.buildLootContext(((ServerWorld) world), itemStack, luck, rand);
            return HeavySieveRegistry.rollSieveRewards(siftable, lootContext);
        }

        return Collections.emptyList();
    }

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.COMMON.autoHeavySieveEnergy.get();
    }

    public float getEffectiveSpeed() {
        return (float) (ExCompressumConfig.COMMON.autoHeavySieveSpeed.get() * getSpeedMultiplier());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.excompressum.auto_heavy_sieve");
    }
}
