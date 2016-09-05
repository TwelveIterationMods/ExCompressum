package net.blay09.mods.excompressum.registry.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SmashableReward {
	private Item item;
	private float chance;
	private int metadata;
	private float luckMultiplier;

	public float getChance() {
		return chance;
	}

	public Item getItem() {
		return item;
	}

	public int getMetadata() {
		return metadata;
	}

	public float getLuckMultiplier() {
		return luckMultiplier;
	}

	public ItemStack createItemStack() { // TODO use me everywhere
		return new ItemStack(item, 1, metadata);
	}
}
