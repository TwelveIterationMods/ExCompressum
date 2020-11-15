package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.registry.GroupedRegistry;
import net.blay09.mods.excompressum.registry.GroupedRegistryData;
import net.blay09.mods.excompressum.registry.RegistryGroup;
import net.blay09.mods.excompressum.registry.RegistryOverride;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.*;

public class HeavySieveRegistry extends GroupedRegistry<
        RegistryGroup,
        RegistryOverride,
        RegistryOverride,
        HeavySiftable,
        GroupedRegistryData<RegistryGroup, RegistryOverride, RegistryOverride, HeavySiftable>> {

    public static final LootParameter<ItemStack> SOURCE_STACK = new LootParameter<>(new ResourceLocation("excompressum", "source_stack"));

    private final Map<ResourceLocation, HeavySiftable> entries = new HashMap<>();

    public HeavySieveRegistry() {
        super("HeavySieve");
    }

    public Collection<HeavySiftable> getEntries() {
        return entries.values();
    }

    public boolean isSiftable(ItemStack itemStack) {
        final ResourceLocation registryName = itemStack.getItem().getRegistryName();
        return entries.containsKey(registryName);
    }

    public boolean isSiftable(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return entries.containsKey(registryName);
    }

    @Nullable
    public HeavySiftable getSiftable(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return entries.get(registryName);
    }

    @Nullable
    public HeavySiftable getSiftable(ItemStack itemStack) {
        final ResourceLocation registryName = itemStack.getItem().getRegistryName();
        return entries.get(registryName);
    }

    // TODO move somewhere else, duplicate code
    public static LootContext buildLootContext(ServerWorld world, ItemStack itemStack, float luck, Random random) {
        return new LootContext.Builder(world)
                .withRandom(random)
                .withParameter(SOURCE_STACK, itemStack)
                .withLuck(luck)
                .build(new LootParameterSet.Builder().required(SOURCE_STACK).build());
    }

    public static List<ItemStack> rollSieveRewards(HeavySiftable siftable, LootContext context) {
        LootTable lootTable = siftable.getLootTable(context);
        if (lootTable != null) {
            return lootTable.generate(context);
        }

        return Collections.emptyList();
    }

    @Override
    protected void reset() {
        super.reset();
        entries.clear();
    }

    @Override
    protected void loadEntry(HeavySiftable entry, @Nullable RegistryOverride groupOverride, @Nullable RegistryOverride entryOverride) {
        entries.put(entry.getSource(), entry);
    }

    @Override
    protected Class<? extends HeavySieveRegistryData> getDataClass() {
        return HeavySieveRegistryData.class;
    }

    @Override
    protected HeavySieveRegistryData getEmptyData() {
        return new HeavySieveRegistryData();
    }
}
