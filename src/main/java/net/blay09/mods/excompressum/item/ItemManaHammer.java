package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.ExRegistro;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;

public class ItemManaHammer extends ToolItem implements IHammer {

	public static final String name = "hammer_mana";
	public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

	private static final int MANA_PER_DAMAGE = 60;

	public ItemManaHammer(Item.Properties properties) {
		super(6f, -3.2f, BotaniaAPI.manasteelToolMaterial, new HashSet<>());
	}

	@Override
	public boolean canHarvestBlock(BlockState state, ItemStack stack) {
		return ExRegistro.isHammerable(state);
	}

	@Override
	public float getDestroySpeed(ItemStack stack, BlockState state) {
		if (ExRegistro.isHammerable(state) && state.getBlock().getHarvestLevel(state) <= toolMaterial.getHarvestLevel()) {
			return efficiency * 0.75f;
		}
		return 0.8f;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		boolean manaRequested = attacker instanceof EntityPlayer && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) attacker, 2 * MANA_PER_DAMAGE, true);
		if (!manaRequested) {
			stack.damageItem(2, attacker);
		}
		return true;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		if (state.getBlockHardness(world, pos) != 0.0) {
			boolean manaRequested = entityLiving instanceof EntityPlayer && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) entityLiving, MANA_PER_DAMAGE, true);
			if (!manaRequested) {
				stack.damageItem(1, entityLiving);
			}
		}
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if(!world.isRemote && entity instanceof EntityPlayer && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) entity, 2 * MANA_PER_DAMAGE, true)) {
			stack.setItemDamage(stack.getItemDamage() - 1);
		}
	}

	@Override
	public boolean usesMana(ItemStack itemStack) {
		return true;
	}

	@Override
	public boolean canHammer(ItemStack itemStack, World world, BlockState state, EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public int getHammerLevel(ItemStack itemStack, World world, BlockState state, EntityPlayer entityPlayer) {
		return toolMaterial.getHarvestLevel();
	}

}
