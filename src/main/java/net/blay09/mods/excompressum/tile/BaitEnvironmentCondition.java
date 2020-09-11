package net.blay09.mods.excompressum.tile;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;

public interface BaitEnvironmentCondition {
    boolean test(BlockState blockState, FluidState fluidState);
}
