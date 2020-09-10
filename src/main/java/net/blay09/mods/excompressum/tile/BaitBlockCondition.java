package net.blay09.mods.excompressum.tile;

import net.minecraft.block.BlockState;

public class BaitBlockCondition {
    private final BlockState state;
    private final boolean isWildcard;

    public BaitBlockCondition(BlockState state, boolean isWildcard) {
        this.state = state;
        this.isWildcard = isWildcard;
    }

    public BlockState getState() {
        return state;
    }
}
