package net.blay09.mods.excompressum.api.sievemesh;

import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class SieveMeshRegistryEntry {
	private final ItemStack itemStack;
	private final Object backingMesh;

	private int meshLevel;
	private boolean isHeavy;
	private String modelName;

	public SieveMeshRegistryEntry(ItemStack itemStack, Object backingMesh) {
		this.itemStack = itemStack;
		this.backingMesh = backingMesh;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public int getMeshLevel() {
		return meshLevel;
	}

	public void setMeshLevel(int meshLevel) {
		this.meshLevel = meshLevel;
	}

	public boolean isHeavy() {
		return isHeavy;
	}

	public void setHeavy(boolean heavy) {
		isHeavy = heavy;
	}

	@Nullable
	public String getModelName() {
		return modelName;
	}

	public void setModelName(@Nullable String modelName) {
		this.modelName = modelName;
	}

	public Object getBackingMesh() {
		return backingMesh;
	}
}
