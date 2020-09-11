package net.blay09.mods.excompressum.tile;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;

public class BaitFluidCondition implements BaitEnvironmentCondition {
    private final Fluid fluid;

    public BaitFluidCondition(Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public boolean test(BlockState blockState, FluidState fluidState) {
        return fluid == fluidState.getFluid();
    }
}
