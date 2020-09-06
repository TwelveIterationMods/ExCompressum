package net.blay09.mods.excompressum.registry;


import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class RegistryKey {
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;

	private final ResourceLocation registryName;
	private int metadata;

	public RegistryKey(ResourceLocation registryName, int metadata) {
		this.registryName = registryName;
		this.metadata = metadata;
	}

	public RegistryKey(ItemStack itemStack) {
		this.registryName = itemStack.getItem().getRegistryName();
		this.metadata = itemStack.getItemDamage();
	}

	public RegistryKey(BlockState state, boolean isWildcard) {
		this.registryName = state.getBlock().getRegistryName();
		this.metadata = isWildcard ? WILDCARD_VALUE : state.getBlock().getMetaFromState(state);
	}

	public RegistryKey withWildcard() {
		metadata = WILDCARD_VALUE;
		return this;
	}

	@Override
	public boolean equals(@Nullable Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RegistryKey that = (RegistryKey) o;

		return metadata == that.metadata && registryName.equals(that.registryName);
	}

	@Override
	public int hashCode() {
		int result = registryName.hashCode();
		result = 31 * result + metadata;
		return result;
	}
}
