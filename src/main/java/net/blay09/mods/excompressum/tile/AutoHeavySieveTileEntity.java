package net.blay09.mods.excompressum.tile;

import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public class AutoHeavySieveTileEntity extends AutoSieveTileEntity {

    public AutoHeavySieveTileEntity() {
        super(ModTileEntities.autoHeavySieve);
    }

    @Override
    public boolean isSiftableWithMesh(ItemStack itemStack, @Nullable SieveMeshRegistryEntry sieveMesh) {
        return ExRegistries.getHeavySieveRegistry().isSiftable(world, getBlockState(), itemStack, sieveMesh);
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        LootContext lootContext = LootTableUtils.buildLootContext(((ServerWorld) world), itemStack, world.rand);
        return HeavySieveRegistry.rollSieveRewards(lootContext, getBlockState(), sieveMesh, itemStack);
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
