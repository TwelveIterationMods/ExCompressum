package net.blay09.mods.excompressum.registry.heavysieve;

import net.minecraft.item.ItemStack;

public class HeavySieveReward {
	private final ItemStack itemStack;
	private final float baseChance;
	@Deprecated
	private final float luckMultiplier;
	private final int meshLevel;

	public HeavySieveReward(ItemStack itemStack, float baseChance, int meshLevel) {
		this.itemStack = itemStack;
		this.baseChance = baseChance;
		this.luckMultiplier = 0f;
		this.meshLevel = meshLevel;
	}

	@Deprecated
	public HeavySieveReward(ItemStack itemStack, float baseChance, float luckMultiplier, int meshLevel) {
		this.itemStack = itemStack;
		this.baseChance = baseChance;
		this.luckMultiplier = luckMultiplier;
		this.meshLevel = meshLevel;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public float getBaseChance() {
		return baseChance;
	}

	@Deprecated
	public float getLuckMultiplier() {
		return luckMultiplier;
	}

	public int getMeshLevel() {
		return meshLevel;
	}
}
