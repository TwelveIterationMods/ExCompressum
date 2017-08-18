package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.List;

public interface IInternalMethods {
	void registerChickenStickHammerable(IBlockState state, boolean isWildcard);
	void registerCompressedHammerEntry(IBlockState state, boolean isWildcard, List<CompressedHammerReward> rewards);
	void registerHeavySieveEntry(IBlockState state, boolean isWildcard, List<HeavySieveReward> rewards);
	void registerWoodenCrucibleEntry(ItemStack itemStack, Fluid fluid, int amount);

	ExNihiloProvider getExNihilo();
}
