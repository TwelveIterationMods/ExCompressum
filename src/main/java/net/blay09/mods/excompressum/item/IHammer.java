package net.blay09.mods.excompressum.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IHammer {
	boolean canHammer(ItemStack itemStack, World world, IBlockState state, EntityPlayer entityPlayer);
	int getHammerLevel(ItemStack itemStack, World world, IBlockState state, EntityPlayer entityPlayer);
}
