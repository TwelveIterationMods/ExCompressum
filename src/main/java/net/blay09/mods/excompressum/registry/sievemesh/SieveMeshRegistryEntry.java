package net.blay09.mods.excompressum.registry.sievemesh;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SieveMeshRegistryEntry {
	private final ItemStack itemStack;
	private final boolean isWildcard;
	private int meshLevel;
	private boolean isHeavy;
	private ResourceLocation spriteLocation;

	public SieveMeshRegistryEntry(ItemStack itemStack, boolean isWildcard) {
		this.itemStack = itemStack;
		this.isWildcard = isWildcard;
	}

	public String getKey() {
		return itemStack.getItem().getRegistryName() + ":" + (isWildcard ? "*" : itemStack.getItemDamage());
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
}
