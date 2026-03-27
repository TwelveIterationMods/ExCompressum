package net.blay09.mods.excompressum.api.sievemesh;

import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

public class SieveMeshRegistryEntry {
	private final CommonMeshType meshType;
	private final ItemStack itemStack;
	private final @Nullable Object backingMesh;

	private boolean isHeavy;
	private @Nullable String modelName;

	public SieveMeshRegistryEntry(CommonMeshType meshType, ItemStack itemStack, @Nullable Object backingMesh) {
		this.meshType = meshType;
		this.itemStack = itemStack;
		this.backingMesh = backingMesh;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public boolean isHeavy() {
		return isHeavy;
	}

	public void setHeavy(boolean heavy) {
		isHeavy = heavy;
	}

	public @Nullable String getModelName() {
		return modelName;
	}

	public void setModelName(@Nullable String modelName) {
		this.modelName = modelName;
	}

	public CommonMeshType getMeshType() {
		return meshType;
	}

	public @Nullable Object getBackingMesh() {
		return backingMesh;
	}
}
