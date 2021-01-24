package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.InternalMethods;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InternalMethodsImpl implements InternalMethods {
	@Override
	public void registerHeavySieveEntry(BlockState state, List<HeavySieveReward> rewards) {
		HeavySieveRegistryEntry entry = new HeavySieveRegistryEntry(state);
		entry.addRewards(rewards);
		// TODO HeavySieveRegistryOld.INSTANCE.add(entry);
	}

	@Override
	public void registerWoodenCrucibleEntry(ItemStack itemStack, Fluid fluid, int amount) {
		// TODO WoodenCrucibleRegistry.INSTANCE.add(new WoodenCrucibleRegistryEntry(itemStack, fluid, amount));
	}
	@Override
	public ExNihiloProvider getExNihilo() {
		return ExNihilo.instance;
	}
}
