package net.blay09.mods.excompressum.registry.hammer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.blay09.mods.excompressum.StupidUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CompressedHammerRegistry {
	private static Map<String, CompressedHammerRegistryEntry> entries = Maps.newHashMap();
	private static Configuration config;

	public static void loadFromConfig(Configuration config) {
		CompressedHammerRegistry.config = config;
		reload();
	}

	public static void reload() {
		// TODO do the thinglemawingles
	}

	private static void registerDefaults() {
		// TODO uhh... think about this first
	}

	public static Map<String, CompressedHammerRegistryEntry> getEntries() {
		return entries;
	}

	public static CompressedHammerRegistryEntry getEntryForBlockState(IBlockState state) {
		String registryName = state.getBlock().getRegistryName().toString();
		CompressedHammerRegistryEntry entry = entries.get(registryName + ":" + state.getBlock().getMetaFromState(state));
		if(entry == null) {
			return entries.get(registryName + ":*");
		}
		return entry;
	}

	public static boolean isHammerable(IBlockState state) {
		return getEntryForBlockState(state) != null;
	}

	public static boolean isHammerable(ItemStack itemStack) {
		IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
		return state != null && isHammerable(state);
	}

	public static Collection<ItemStack> rollHammerRewards(IBlockState state, float luck, Random rand) {
		CompressedHammerRegistryEntry entry = getEntryForBlockState(state);
		if(entry != null) {
			List<ItemStack> list = Lists.newArrayList();
			for(CompressedHammerReward reward : entry.getRewards()) {
				int chance = reward.getBaseChance() + (int) (reward.getLuckMultiplier() * luck);
				if(rand.nextInt(100) < chance) {
					list.add(reward.getItemStack().copy());
				}
			}
			return list;
		}
		return Collections.emptyList();
	}

	public static Collection<ItemStack> rollHammerRewards(ItemStack itemStack, float luck, Random rand) {
		IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
		if(state != null) {
			return rollHammerRewards(state, luck, rand);
		}
		return Collections.emptyList();
	}

}
