package net.blay09.mods.excompressum.registry.heavysieve;

import net.minecraft.item.ItemStack;

public class HeavySieveReward {
	private final ItemStack itemStack;
	private final float baseChance;
	private final int meshLevel;

	public HeavySieveReward(ItemStack itemStack, float baseChance, int meshLevel) {
		this.itemStack = itemStack;
		this.baseChance = baseChance;
		this.meshLevel = meshLevel;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public float getBaseChance() {
		return baseChance;
	}

	public int getMeshLevel() {
		return meshLevel;
	}
}
