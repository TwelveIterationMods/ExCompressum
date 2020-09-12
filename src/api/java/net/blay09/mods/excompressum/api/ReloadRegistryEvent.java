package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;

public class ReloadRegistryEvent extends Event {

	/**
	 * When handling this event, register blocks that should be hammerable by the Chicken Stick.
	 */
	public static class ChickenStick extends ReloadRegistryEvent {
		public void register(BlockState state, boolean isWildcard) {
			ExCompressumAPI.registerChickenStickHammerable(state);
		}
	}

	/**
	 * When handling this event, register blocks that should be hammerable by the Compressed Hammer.
	 */
	public static class CompressedHammer extends ReloadRegistryEvent {
		/**
		 * To add rewards, call addReward() on the returned object, while instantiating new CompressedHammerReward objects
		 */
		public void register(BlockState state, boolean isWildcard, List<CompressedHammerReward> rewards) {
			ExCompressumAPI.registerCompressedHammerEntry(state, rewards);
		}
	}

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
