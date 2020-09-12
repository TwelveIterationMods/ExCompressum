package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.InternalMethods;
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
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InternalMethodsImpl implements InternalMethods {
	@Override
	public void registerChickenStickHammerable(BlockState state) {
		ChickenStickRegistry.INSTANCE.add(state);
	}

	@Override
	public void registerCompressedHammerEntry(BlockState state, List<CompressedHammerReward> rewards) {
		CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(state);
		entry.addRewards(rewards);
		CompressedHammerRegistry.INSTANCE.add(entry);
	}

	@Override
	public void registerHeavySieveEntry(BlockState state, List<HeavySieveReward> rewards) {
		HeavySieveRegistryEntry entry = new HeavySieveRegistryEntry(state);
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
