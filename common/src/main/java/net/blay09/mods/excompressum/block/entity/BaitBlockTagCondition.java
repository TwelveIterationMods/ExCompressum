package net.blay09.mods.excompressum.block.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class BaitBlockTagCondition implements BaitEnvironmentCondition {
    private final TagKey<Block> tag;
    private final Component displayName;

    public BaitBlockTagCondition(TagKey<Block> tag, Component displayName) {
        this.tag = tag;
        this.displayName = displayName;
    }

    @Override
    public boolean test(BlockState blockState, FluidState fluidState) {
        return blockState.is(tag);
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }
}
