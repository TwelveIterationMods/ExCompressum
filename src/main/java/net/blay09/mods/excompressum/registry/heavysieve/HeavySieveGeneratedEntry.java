package net.blay09.mods.excompressum.registry.heavysieve;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class HeavySieveGeneratedEntry {
	private final ItemStack itemStack;
	private final ResourceLocation source;
	private final int sourceMetadata;
	private final int sourceCount;

	public HeavySieveGeneratedEntry(ItemStack itemStack, ResourceLocation source, int sourceMetadata, int sourceCount) {
		this.itemStack = itemStack;
		this.source = source;
		this.sourceMetadata = sourceMetadata;
		this.sourceCount = sourceCount;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public ResourceLocation getSource() {
		return source;
	}

	public int getSourceMetadata() {
		return sourceMetadata;
	}

	public int getSourceCount() {
		return sourceCount;
	}
}
