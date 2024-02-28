package net.blay09.mods.excompressum.block;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Locale;

public enum WoodenCrucibleType implements StringRepresentable {
    OAK(Blocks.OAK_LOG),
    SPRUCE(Blocks.SPRUCE_LOG),
    BIRCH(Blocks.BIRCH_LOG),
    JUNGLE(Blocks.JUNGLE_LOG),
    ACACIA(Blocks.ACACIA_LOG),
    DARK_OAK(Blocks.DARK_OAK_LOG);

    public static WoodenCrucibleType[] values = values();

    private final Block baseBlock;

    WoodenCrucibleType(Block baseBlock) {
        this.baseBlock = baseBlock;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ENGLISH);
    }

    public Block getBaseBlock() {
        return baseBlock;
    }
}
