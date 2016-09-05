package net.blay09.mods.excompressum.registry.data;

import net.minecraft.block.Block;

public class SiftingResult {
	private int rarity;
	private Block item;
	private int metadata;

	public int getRarity() {
		return rarity;
	}

	public Block getItem() {
		return item;
	}

	public int getMetadata() {
		return metadata;
	}
}
