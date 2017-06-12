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
	 * @param entity
	 * @return
	 */
	public static boolean hasSilkTouchModifier(EntityLivingBase entity) {
		ItemStack heldItem = entity.getHeldItemMainhand();
		return !heldItem.isEmpty() && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) > 0;
	}

	/**
	 * Wish this would just be part of ItemBlock itself.
	 * @param itemStack
	 * @return
	 */
	@Nullable
	@SuppressWarnings("deprecation")
	public static IBlockState getStateFromItemStack(ItemStack itemStack) {
		if(itemStack.getItem() instanceof ItemBlock) {
			Block block = ((ItemBlock) itemStack.getItem()).block;
			try {
				int meta = itemStack.getItem().getMetadata(itemStack.getItemDamage());
				return block.getStateFromMeta(meta);
			} catch (Exception e) {
				// TODO do this before an official release
				// Hacky workaround. In 1.11, don't use block states as identifiers for the registries, as they are a pain to retrieve from an item stack.
			}
			return block.getDefaultState();
		}
		return null;
	}

	/**
	 * Exists as createStackedBlock but it's protected, thanks Notch
	 * @param state
	 * @return
	 */
	public static ItemStack getItemStackFromState(IBlockState state) {
		Item item = Item.getItemFromBlock(state.getBlock());
		if(item != Items.AIR) {
			return new ItemStack(item, 1, state.getBlock().getMetaFromState(state)); // this could break but it's fineeee
		}
		return ItemStack.EMPTY;
	}
}
