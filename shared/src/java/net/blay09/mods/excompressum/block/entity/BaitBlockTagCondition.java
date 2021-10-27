package net.blay09.mods.excompressum.block.entity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class BaitBlockTagCondition implements BaitEnvironmentCondition {
    private final ResourceLocation tag;

    public BaitBlockTagCondition(ResourceLocation tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(BlockState blockState, FluidState fluidState) {
        return blockState.getBlock().getTags().contains(tag);
    }
}
