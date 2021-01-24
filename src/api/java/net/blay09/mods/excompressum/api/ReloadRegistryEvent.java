package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class ReloadRegistryEvent extends Event {

	/**
	 * When handling this event, register blocks that should be siftable by the Heavy Sieve.
	 */
	public static class HeavySieve extends ReloadRegistryEvent {
		public void register(BlockState state, boolean isWildcard, List<HeavySieveReward> rewards) {
			ExCompressumAPI.registerHeavySieveEntry(state, rewards);
		}
	}

	/**
	 * When handling this event, register blocks that should be meltable by the Wooden Crucible.
	 */
	public static class WoodenCrucible extends ReloadRegistryEvent {
		public void register(ItemStack itemStack, Fluid fluid, int amount) {
			ExCompressumAPI.registerWoodenCrucibleEntry(itemStack, fluid, amount);
		}
	}
}
