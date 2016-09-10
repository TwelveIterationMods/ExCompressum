package net.blay09.mods.excompressum.registry.compressedhammer;

import net.minecraft.item.ItemStack;

public class CompressedHammerReward {
	private final ItemStack itemStack;
	private final float baseChance;
	private final float luckMultiplier;

	public CompressedHammerReward(ItemStack itemStack, float baseChance, float luckMultiplier) {
		this.itemStack = itemStack;
		this.baseChance = baseChance;
		this.luckMultiplier = luckMultiplier;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public float getBaseChance() {
		return baseChance;
	}

	public float getLuckMultiplier() {
		return luckMultiplier;
	}
}
