package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;

import java.util.HashSet;

public class ItemManaHammer extends ItemTool implements IManaUsingItem, IHammer {

	private static final int MANA_PER_DAMAGE = 60;

	protected ItemManaHammer() {
		super(6f, -3.2f, BotaniaAPI.manasteelToolMaterial, new HashSet<Block>());
		setRegistryName("hammer_mana");
		setUnlocalizedName(getRegistryName().toString());
		setCreativeTab(ExCompressum.creativeTab);
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
		return ExRegistro.isHammerable(state);
	}

	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		if (ExRegistro.isHammerable(state) && state.getBlock().getHarvestLevel(state) <= toolMaterial.getHarvestLevel()) {
			return efficiencyOnProperMaterial * 0.75f;
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
	public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
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
	public boolean canHammer(ItemStack itemStack, World world, IBlockState state, EntityPlayer entityPlayer) {
		return true;
	}

	@Override
	public int getHammerLevel(ItemStack itemStack, World world, IBlockState state, EntityPlayer entityPlayer) {
		return toolMaterial.getHarvestLevel();
	}
}
