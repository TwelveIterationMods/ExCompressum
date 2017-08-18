package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.IInternalMethods;
import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerRegistryEntry;
import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.api.woodencrucible.WoodenCrucibleRegistryEntry;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import java.util.List;

public class InternalMethods implements IInternalMethods {
	@Override
	public void registerChickenStickHammerable(IBlockState state, boolean isWildcard) {
		ChickenStickRegistry.INSTANCE.add(state, isWildcard);
	}

	@Override
	public void registerCompressedHammerEntry(IBlockState state, boolean isWildcard, List<CompressedHammerReward> rewards) {
		CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(state, isWildcard);
		entry.addRewards(rewards);
		CompressedHammerRegistry.INSTANCE.add(entry);
	}

	@Override
	public void registerHeavySieveEntry(IBlockState state, boolean isWildcard, List<HeavySieveReward> rewards) {
		HeavySieveRegistryEntry entry = new HeavySieveRegistryEntry(state, isWildcard);
		entry.addRewards(rewards);
		HeavySieveRegistry.INSTANCE.add(entry);
	}

	@Override
	public void registerWoodenCrucibleEntry(ItemStack itemStack, Fluid fluid, int amount) {
		WoodenCrucibleRegistry.INSTANCE.add(new WoodenCrucibleRegistryEntry(itemStack, fluid, amount));
	}
	@Override
	public ExNihiloProvider getExNihilo() {
		return ExRegistro.instance;
	}
}
