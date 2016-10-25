package net.blay09.mods.excompressum.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public class StupidUtils {

	/**
	 * see https://github.com/MinecraftForge/MinecraftForge/pull/2793
	 * @param world
	 * @param pos
	 * @return
	 */
	public static int getComparatorOutput64(World world, BlockPos pos) {
		TileEntity tileEntity = world.getTileEntity(pos);
		if(tileEntity != null) {
			//noinspection ConstantConditions /// Forge.
			return getComparatorOutput64(tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
		}
		return 0;
	}

	/**
	 * Removed from Vanilla's EnchantmentHelper for some stupid reason.
	 * @param entity
	 * @return
	 */
	public static boolean hasSilkTouchModifier(EntityLivingBase entity) {
		ItemStack heldItem = entity.getHeldItemMainhand();
		return heldItem != null && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, heldItem) > 0;
	}

	private static int getComparatorOutput64(@Nullable IItemHandler itemHandler) {
		if (itemHandler != null) {
			int i = 0;
			float f = 0f;
			for (int j = 0; j < itemHandler.getSlots(); ++j) {
				ItemStack itemstack = itemHandler.getStackInSlot(j);
				if (itemstack != null) {
					f += (float) itemstack.stackSize / (float) Math.min(64, itemstack.getMaxStackSize());
					i++;
				}
			}
			f = f / (float) itemHandler.getSlots();
			return MathHelper.floor_float(f * 14f) + (i > 0 ? 1 : 0);
		}
		return 0;
	}

	/**
	 * Wish this would just be part of ItemBlock itself.
	 * @param itemStack
	 * @return
	 */
	@Nullable
	public static IBlockState getStateFromItemStack(ItemStack itemStack) {
		if(itemStack.getItem() instanceof ItemBlock) {
			Block block = ((ItemBlock) itemStack.getItem()).block;
			int meta = itemStack.getItem().getMetadata(itemStack.getItemDamage()); // TODO update Forge, getStateForPlacement
			return block.getStateFromMeta(meta);
		}
		return null;
	}

	/**
	 * Exists as createStackedBlock but it's protected, thanks Notch
	 * @param state
	 * @return
	 */
	@Nullable
	public static ItemStack getItemStackFromState(IBlockState state) {
		Item item = Item.getItemFromBlock(state.getBlock());
		if(item != null) {
			return new ItemStack(item, 1, state.getBlock().getMetaFromState(state)); // this could break but it's fineeee
		}
		return null;
	}
}
