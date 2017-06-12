package net.blay09.mods.excompressum.item;

import net.minecraft.item.Item;

public class ItemCompressum extends Item {

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
