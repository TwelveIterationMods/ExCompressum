package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.ExRegistries;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Collection;

public class AutoCompressedHammerBlockEntity extends AutoHammerBlockEntity {

    public AutoCompressedHammerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.autoCompressedHammer.get(), pos, state);
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
    public boolean isRegistered(ItemStack itemStack) {
        RecipeManager recipeManager = ExCompressum.proxy.getRecipeManager(level);
        return ExRegistries.getCompressedHammerRegistry().isHammerable(recipeManager, itemStack);
    }

    @Override
    public Collection<ItemStack> rollHammerRewards(ItemStack itemStack, ItemStack toolItem, RandomSource rand) {
        LootContext lootContext = LootTableUtils.buildLootContext(((ServerLevel) level), itemStack);
        return CompressedHammerRegistry.rollHammerRewards(level, lootContext, itemStack);
    }

    @Override
    public boolean isHammerUpgrade(ItemStack itemStack) {
        if (itemStack.getItem() == ModItems.diamondCompressedHammer) {
            return true;
        }
        if (itemStack.getItem() == Compat.TCONSTRUCT_HAMMER) {
            CompoundTag tagCompound = itemStack.getTag();
            if (tagCompound != null) {
                ListTag traits = tagCompound.getList("Traits", Tag.TAG_STRING);
                for (Tag tag : traits) {
                    if (tag.getAsString().equalsIgnoreCase(Compat.TCONSTRUCT_TRAIT_SMASHINGII)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.excompressum.auto_compressed_hammer");
    }
}
