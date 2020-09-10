package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface InternalMethods {
	void registerChickenStickHammerable(BlockState state, boolean isWildcard);
	void registerCompressedHammerEntry(BlockState state, boolean isWildcard, List<CompressedHammerReward> rewards);
	void registerHeavySieveEntry(BlockState state, boolean isWildcard, List<HeavySieveReward> rewards);
	void registerWoodenCrucibleEntry(ItemStack itemStack, Fluid fluid, int amount);

	ExNihiloProvider getExNihilo();
}
