package net.blay09.mods.excompressum.api.sievemesh;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SieveMeshRegistryEntry {
	private final ItemStack itemStack;
	private int meshLevel;
	private boolean isHeavy;
	private ResourceLocation spriteLocation;
	private Object backingMesh;

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
	public ResourceLocation getSpriteLocation() {
		return spriteLocation;
	}

	public void setSpriteLocation(ResourceLocation spriteLocation) {
		this.spriteLocation = spriteLocation;
	}

	public Object getBackingMesh() {
		return backingMesh;
	}
}
