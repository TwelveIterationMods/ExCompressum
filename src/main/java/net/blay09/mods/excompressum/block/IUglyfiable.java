package net.blay09.mods.excompressum.block;


import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public interface IUglyfiable {
    boolean uglify(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state, Hand hand, Direction facing, Vector3d hitVec);
}
