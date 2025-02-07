package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class BaitFluidCondition implements BaitEnvironmentCondition {
    private final Fluid fluid;
    public BaitFluidCondition(Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public boolean test(BlockState blockState, FluidState fluidState) {
        return fluid == fluidState.getType();
    }

    @Override
    public Component getDisplayName() {
        final var registryName = Balm.getRegistries().getKey(fluid);
        return Component.translatable("fluid_type." + registryName.toString().replace(':', '.'));
    }
}
