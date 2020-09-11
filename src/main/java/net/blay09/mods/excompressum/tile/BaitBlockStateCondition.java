package net.blay09.mods.excompressum.tile;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;

public class BaitBlockStateCondition implements BaitEnvironmentCondition {
    private final BlockState state;

    public BaitBlockStateCondition(BlockState state) {
        this.state = state;
    }

    @Override
    public boolean test(BlockState blockState, FluidState fluidState) {
        return state == blockState;
    }
}
