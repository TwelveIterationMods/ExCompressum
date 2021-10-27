package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public class AutoHeavySieveBlockEntity extends AutoSieveBlockEntity {

    public AutoHeavySieveBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.autoHeavySieve.get(), pos, state);
    }

    @Override
    public boolean isSiftableWithMesh(ItemStack itemStack, @Nullable SieveMeshRegistryEntry sieveMesh) {
        return ExRegistries.getHeavySieveRegistry().isSiftable(level, getBlockState(), itemStack, sieveMesh);
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        LootContext lootContext = LootTableUtils.buildLootContext(((ServerLevel) level), itemStack, rand);
        return HeavySieveRegistry.rollSieveRewards(lootContext, getBlockState(), sieveMesh, itemStack);
    }

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.getActive().automation.autoHeavySieveEnergy;
    }

    public float getEffectiveSpeed() {
        return (float) (ExCompressumConfig.getActive().automation.autoHeavySieveSpeed * getSpeedMultiplier());
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.excompressum.auto_heavy_sieve");
    }
}
