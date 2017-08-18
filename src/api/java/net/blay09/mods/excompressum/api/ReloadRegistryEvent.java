package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerRegistryEntry;
import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

public class ReloadRegistryEvent extends Event {

	/**
	 * When handling this event, register blocks that should be hammerable by the Chicken Stick.
	 */
	public static class ChickenStick extends ReloadRegistryEvent {
		public void register(IBlockState state, boolean isWildcard) {
			ExCompressumAPI.__internalMethods.registerChickenStickHammerable(state, isWildcard);
		}
	}

	/**
	 * When handling this event, register blocks that should be hammerable by the Compressed Hammer.
	 */
	public static class CompressedHammer extends ReloadRegistryEvent {
		/**
		 * To add rewards, call addReward() on the returned object, while instantiating new CompressedHammerReward objects
		 */
		public void register(IBlockState state, boolean isWildcard, List<CompressedHammerReward> rewards) {
			ExCompressumAPI.__internalMethods.registerCompressedHammerEntry(state, isWildcard, rewards);
		}
	}

	/**
	 * When handling this event, register blocks that should be siftable by the Heavy Sieve.
	 */
	public static class HeavySieve extends ReloadRegistryEvent {
		public void register(IBlockState state, boolean isWildcard, List<HeavySieveReward> rewards) {
			ExCompressumAPI.__internalMethods.registerHeavySieveEntry(state, isWildcard, rewards);
		}
	}

	/**
	 * When handling this event, register blocks that should be meltable by the Wooden Crucible.
	 */
	public static class WoodenCrucible extends ReloadRegistryEvent {
		public void register(ItemStack itemStack, Fluid fluid, int amount) {
			ExCompressumAPI.__internalMethods.registerWoodenCrucibleEntry(itemStack, fluid, amount);
		}
	}
}
