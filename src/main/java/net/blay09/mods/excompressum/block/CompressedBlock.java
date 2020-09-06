package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import net.minecraft.util.ResourceLocation;

public class CompressedBlock extends Block {

	public static final String name = "compressed_block";
	public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

	public CompressedBlock(CompressedBlockType type) {
		super(Material.ROCK);
		setHardness(4f);
		setResistance(6f);
		setSoundType(SoundType.STONE);
		setCreativeTab(ExCompressum.creativeTab);
	}

}
