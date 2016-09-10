package net.blay09.mods.excompressum.registry.heavysieve;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.StupidUtils;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.RegistryKey;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HeavySieveRegistry extends AbstractRegistry {

    public static final HeavySieveRegistry INSTANCE = new HeavySieveRegistry();
    private static final Map<RegistryKey, HeavySieveRegistryEntry> entries = Maps.newHashMap();

    public HeavySieveRegistry() {
        super("HeavySieve");
    }

    @Nullable
    public static HeavySieveRegistryEntry getEntryForBlockState(IBlockState state, boolean withWildcard) {
        return entries.get(new RegistryKey(state, withWildcard));
    }

    public static boolean isSiftable(IBlockState state) {
        return getEntryForBlockState(state, false) != null || getEntryForBlockState(state, true) != null;
    }

    public static boolean isSiftable(ItemStack itemStack) {
        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && isSiftable(state);
    }

    public static Map<RegistryKey, HeavySieveRegistryEntry> getEntries() {
        return entries;
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
            if(rand.nextFloat() < reward.getBaseChance() + reward.getLuckMultiplier() * luck) {
                list.add(reward.getItemStack().copy());
            }
        }
    }

    @Override
    protected JsonObject create() {
        return null;
    }

    @Override
    protected void loadCustom(JsonObject entry) {

    }

    @Override
    protected void registerDefaults(JsonObject defaults) {

    }
}
