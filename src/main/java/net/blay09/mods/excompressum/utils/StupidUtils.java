package net.blay09.mods.excompressum.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class StupidUtils {

	/**
	 * Removed from Vanilla's EnchantmentHelper for some stupid reason.
	 */
	public static boolean hasSilkTouchModifier(EntityLivingBase entity) {
		ItemStack heldItem = entity.getHeldItemMainhand();
		return !heldItem.isEmpty() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) > 0;
	}

	/**
	 * Wish this would just be part of ItemBlock itself.
	 */
	@Nullable
	@SuppressWarnings("deprecation")
	public static IBlockState getStateFromItemStack(ItemStack itemStack) {
		if(itemStack.getItem() instanceof ItemBlock) {
			Block block = ((ItemBlock) itemStack.getItem()).getBlock();
			try {
				int meta = itemStack.getItem().getMetadata(itemStack.getItemDamage());
				return block.getStateFromMeta(meta);
			} catch (Exception e) {
				// In case of weirdness, don't crash! Just fallback to default.
			}
			return block.getDefaultState();
		}
		return null;
	}

	public static ItemStack getItemStackFromState(IBlockState state) {
		Item item = Item.getItemFromBlock(state.getBlock());
		if(item != Items.AIR) {
			return new ItemStack(item, 1, state.getBlock().damageDropped(state));
		}
		return ItemStack.EMPTY;
	}
}
