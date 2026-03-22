package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Collection;

public class AutoCompressedHammerBlockEntity extends AutoHammerBlockEntity {

    public AutoCompressedHammerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.autoCompressedHammer.value(), pos, state);
    }

    @Override
    public int getEffectiveEnergy() {
        return ExCompressumConfig.getActive().automation.autoCompressedHammerEnergy;
    }

    @Override
    public float getEffectiveSpeed() {
        return (float) (ExCompressumConfig.getActive().automation.autoCompressedHammerSpeed * getSpeedMultiplier());
    }

    @Override
    public boolean isRegistered(ServerLevel level, ItemStack itemStack) {
        final var recipeManager = level.getServer().getRecipeManager();
        return ExRegistries.getCompressedHammerRegistry().isHammerable(recipeManager, itemStack);
    }

    @Override
    public Collection<ItemStack> rollHammerRewards(ServerLevel level, ItemStack itemStack, ItemStack toolItem, RandomSource rand) {
        LootContext lootContext = LootTableUtils.buildLootContext(level, itemStack);
        return CompressedHammerRegistry.rollHammerRewards(level, lootContext, itemStack);
    }

    @Override
    public boolean isHammerUpgrade(ItemStack itemStack) {
        return itemStack.is(ModItemTags.COMPRESSED_HAMMERS);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("item.excompressum.auto_compressed_hammer");
    }
}
