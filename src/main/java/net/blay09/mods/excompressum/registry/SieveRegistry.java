package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.registry.data.SiftingResult;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class SieveRegistry {
	public static boolean isRegistered(Block blockFromItem, int itemDamage) { // TODO uh
		return false;
	}

	public static Collection<SiftingResult> getSiftingOutput(ItemStack itemStack) {
		return new ArrayList<>();
	}
}
