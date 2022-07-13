package net.blay09.mods.excompressum.block.entity;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class BaitBlockTagCondition implements BaitEnvironmentCondition {
    private final TagKey<Block> tag;

    public BaitBlockTagCondition(TagKey<Block> tag) {
        this.tag = tag;
    }

    @Override
    public boolean test(BlockState blockState, FluidState fluidState) {
        return blockState.is(tag);
    }
}
