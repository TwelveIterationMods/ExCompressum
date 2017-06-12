package net.blay09.mods.excompressum.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemTool;

import java.util.Set;

public class ItemCompressumTool extends ItemTool {

	protected ItemCompressumTool(float attackDamageIn, float attackSpeedIn, ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
		super(attackDamageIn, attackSpeedIn, materialIn, effectiveBlocksIn);
	}

	protected ItemCompressumTool(ToolMaterial materialIn, Set<Block> effectiveBlocksIn) {
		super(materialIn, effectiveBlocksIn);
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
