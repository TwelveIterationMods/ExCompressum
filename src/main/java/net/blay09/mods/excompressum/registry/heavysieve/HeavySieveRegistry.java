package net.blay09.mods.excompressum.registry.heavysieve;

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

public class HeavySieveRegistry {

    private static final Map<String, HeavySieveRegistryEntry> entries = Maps.newHashMap();
    private static Configuration config;

    public static void loadFromConfig(Configuration config) {
        HeavySieveRegistry.config = config;
        reload();
    }

    public static void reload() {
        // TODO do the thinglemawingles
    }

    private static void registerDefaults() {
        int defaultLoss = 3; // TODO config option

        // TODO uhh... think about this first

        // TODO additional compressed block mapping?
    }

    public static Map<String, HeavySieveRegistryEntry> getEntries() {
        return entries;
    }

    public static HeavySieveRegistryEntry getEntryForBlockState(IBlockState state, boolean isWildcard) {
        String registryName = state.getBlock().getRegistryName().toString();
        if(isWildcard) {
            return entries.get(registryName + ":*");
        } else {
            return entries.get(registryName + ":" + state.getBlock().getMetaFromState(state));
        }
    }

    public static boolean isSiftable(IBlockState state) {
        return getEntryForBlockState(state, true) != null || getEntryForBlockState(state, false) != null;
    }

    public static boolean isSiftable(ItemStack itemStack) {
        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && isSiftable(state);
    }

    public static Collection<ItemStack> rollSieveRewards(IBlockState state, float luck, Random rand) {
        List<ItemStack> list = Lists.newArrayList();
        HeavySieveRegistryEntry genericEntry = getEntryForBlockState(state, true);
        if(genericEntry != null) {
            rollSieveRewardsToList(genericEntry, list, luck, rand);
        }
        HeavySieveRegistryEntry entry = getEntryForBlockState(state, false);
        if(entry != null) {
            rollSieveRewardsToList(entry, list, luck, rand);
        }
        return list;
    }

    public static Collection<ItemStack> rollSieveRewards(ItemStack itemStack, float luck, Random rand) {
        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
        if(state != null) {
            return rollSieveRewards(state, luck, rand);
        }
        return Collections.emptyList();
    }

    private static void rollSieveRewardsToList(HeavySieveRegistryEntry entry, List<ItemStack> list, float luck, Random rand) {
        for(HeavySieveReward reward : entry.getRewards()) {
            if(rand.nextInt(100) < reward.getBaseChance() + reward.getLuckMultiplier() * luck) {
                list.add(reward.getItemStack().copy());
            }
        }
    }

}
