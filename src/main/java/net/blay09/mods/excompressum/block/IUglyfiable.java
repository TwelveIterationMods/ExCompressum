package net.blay09.mods.excompressum.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IUglyfiable {
	boolean uglify(EntityPlayer player, World world, BlockPos pos, IBlockState state, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ);
}
