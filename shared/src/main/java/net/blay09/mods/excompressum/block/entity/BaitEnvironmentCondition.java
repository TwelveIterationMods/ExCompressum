package net.blay09.mods.excompressum.block.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public interface BaitEnvironmentCondition {
    boolean test(BlockState blockState, FluidState fluidState);
    Component getDisplayName();
}
