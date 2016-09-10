package net.blay09.mods.excompressum.registry.heavysieve;

import net.minecraft.item.ItemStack;

public class HeavySieveReward {
	private final ItemStack itemStack;
	private final int baseChance;
	private final int luckMultiplier;

	public HeavySieveReward(ItemStack itemStack, int baseChance, int luckMultiplier) {
		this.itemStack = itemStack;
		this.baseChance = baseChance;
		this.luckMultiplier = luckMultiplier;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public int getBaseChance() {
		return baseChance;
	}

	public int getLuckMultiplier() {
		return luckMultiplier;
	}
}
