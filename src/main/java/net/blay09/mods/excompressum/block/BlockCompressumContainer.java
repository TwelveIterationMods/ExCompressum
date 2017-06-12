package net.blay09.mods.excompressum.block;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;

public abstract class BlockCompressumContainer extends BlockContainer {

	protected BlockCompressumContainer(Material material) {
		super(material);
	}

	/**
	 * Simple wrapper around getRegistryName().toString() to get rid of nullable warnings.
	 * Our registry names are always set by the time we call this function.
	 * @return the registry name as a string
	 */
	public String getRegistryNameString() {
		//noinspection ConstantConditions
		return getRegistryName().toString();
	}

}
