package net.blay09.mods.excompressum.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class CompressedBlock extends Block {

    public static final String namePrefix = "compressed_";

    private final CompressedBlockType type;

    public CompressedBlock(CompressedBlockType type) {
        super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(4f, 6f));
        this.type = type;
    }

}
