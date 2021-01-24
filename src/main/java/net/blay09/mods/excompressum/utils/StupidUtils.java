package net.blay09.mods.excompressum.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import javax.annotation.Nullable;

public class StupidUtils {

    /**
     * Removed from Vanilla's EnchantmentHelper for some stupid reason.
     */
    public static boolean hasSilkTouchModifier(LivingEntity entity) {
        return EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.SILK_TOUCH, entity) > 0;
    }

    /**
     * Wish this would just be part of ItemBlock itself.
     */
    @Nullable
    public static BlockState getStateFromItemStack(ItemStack itemStack) {
        if (itemStack.getItem() instanceof BlockItem) {
            Block block = ((BlockItem) itemStack.getItem()).getBlock();
            try {
                return block.getDefaultState();
            } catch (Exception e) {
                // In case of weirdness, don't crash! Just fallback to default.
            }
            return block.getDefaultState();
        }
        return Blocks.AIR.getDefaultState();
    }

    public static ItemStack getItemStackFromState(BlockState state) {
        Item item = state.getBlock().asItem();
        if (item != Items.AIR) {
            return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }

}
