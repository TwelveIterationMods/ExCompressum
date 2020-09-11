package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * To register stuff, subscribe to the appropriate {@link net.blay09.mods.excompressum.api.ReloadRegistryEvent}.
 */
public class ExCompressumAPI {
	private static InternalMethods internalMethods;

	/**
	 * INTERNAL USE ONLY
	 */
	public static void __setupAPI(InternalMethods internalMethods) {
		ExCompressumAPI.internalMethods = internalMethods;
	}

	public static ExNihiloProvider getExNihilo() {
		return internalMethods.getExNihilo();
	}

	public static void registerChickenStickHammerable(BlockState state, boolean isWildcard) {
		internalMethods.registerChickenStickHammerable(state, isWildcard);
	}

	public static void registerCompressedHammerEntry(BlockState state, boolean isWildcard, List<CompressedHammerReward> rewards) {
		internalMethods.registerCompressedHammerEntry(state, isWildcard, rewards);
	}

	public static void registerHeavySieveEntry(BlockState state, boolean isWildcard, List<HeavySieveReward> rewards) {
		internalMethods.registerHeavySieveEntry(state, isWildcard, rewards);
	}

	public static void registerWoodenCrucibleEntry(ItemStack itemStack, Fluid fluid, int amount) {
		internalMethods.registerWoodenCrucibleEntry(itemStack, fluid, amount);
	}
}
