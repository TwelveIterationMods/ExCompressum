package net.blay09.mods.excompressum.block;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

public interface IUglyfiable {
    boolean uglify(@Nullable Player player, Level level, BlockPos pos, BlockState state, InteractionHand hand, Direction facing, Vec3 hitVec);
}
