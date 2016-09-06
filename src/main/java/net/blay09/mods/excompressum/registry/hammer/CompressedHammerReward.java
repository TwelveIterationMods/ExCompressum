package net.blay09.mods.excompressum.registry.hammer;

import net.minecraft.item.ItemStack;

public class CompressedHammerReward {
	private final ItemStack itemStack;
	private final int baseChance;
	private final int luckMultiplier;

	public CompressedHammerReward(ItemStack itemStack, int baseChance, int luckMultiplier) {
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
