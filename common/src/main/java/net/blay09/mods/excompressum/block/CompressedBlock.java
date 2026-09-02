package net.blay09.mods.excompressum.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;

public class CompressedBlock extends Block {

    private final CompressedBlockType type;

    public CompressedBlock(CompressedBlockType type, Properties properties) {
        super(properties.sound(SoundType.STONE).strength(4f, 6f));
        this.type = type;
    }

    public CompressedBlockType getType() {
        return type;
    }

}
