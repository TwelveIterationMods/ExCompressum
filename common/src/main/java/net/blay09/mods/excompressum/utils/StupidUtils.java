package net.blay09.mods.excompressum.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class StupidUtils {

    /**
     * Removed from Vanilla's EnchantmentHelper for some stupid reason.
     */
    public static boolean hasSilkTouchModifier(LivingEntity entity) {
        final var silkTouch = entity.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
        return EnchantmentHelper.getEnchantmentLevel(silkTouch, entity) > 0;
    }

    public static boolean hasSilkTouchModifier(Level level, ItemInstance itemStack) {
        final var silkTouch = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
        return EnchantmentHelper.getItemEnchantmentLevel(silkTouch, itemStack) > 0;
    }

    /**
     * Wish this would just be part of ItemBlock itself.
     */
    public static BlockState getStateFromItemStack(ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) itemStack.getItem()).getBlock();
            try {
                return block.defaultBlockState();
            } catch (Exception e) {
                // In case of weirdness, don't crash! Just fallback to default.
            }
            return block.defaultBlockState();
        }
        return Blocks.AIR.defaultBlockState();
    }

    public static ItemStack getItemStackFromState(BlockState state) {
        Item item = state.getBlock().asItem();
        if (item != Items.AIR) {
            return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }

}
