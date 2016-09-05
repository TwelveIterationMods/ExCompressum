package net.blay09.mods.excompressum.registry.data;

import net.minecraft.block.Block;

public class SiftingResult {
	private float rarity;
	private Block item;
	private int metadata;

	public float getRarity() {
		return rarity;
	}

	public Block getItem() {
		return item;
	}

	public int getMetadata() {
		return metadata;
	}
}
