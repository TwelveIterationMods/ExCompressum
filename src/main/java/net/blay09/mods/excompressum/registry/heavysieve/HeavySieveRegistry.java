package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.registry.*;
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
        HeavySieveRegistryData> {

    public static final LootParameter<ItemStack> SOURCE_STACK = new LootParameter<>(new ResourceLocation("excompressum", "source_stack"));

    private final Map<ResourceLocation, HeavySiftable> entries = new HashMap<>();
    private final Map<ResourceLocation, GeneratedHeavySiftable> generatedEntries = new HashMap<>();
    private final Map<ResourceLocation, HeavySiftable> generatedEntriesCache = new HashMap<>();

    public HeavySieveRegistry() {
        super("HeavySieve");
    }

    public Collection<HeavySiftable> getEntries() {
        return entries.values();
    }

    public boolean isSiftable(ItemStack itemStack) {
        final ResourceLocation registryName = itemStack.getItem().getRegistryName();
        return getSiftable(Objects.requireNonNull(registryName)) != null;
    }

    public boolean isSiftable(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return getSiftable(Objects.requireNonNull(registryName)) != null;
    }

    @Nullable
    public HeavySiftable getSiftable(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return getSiftable(Objects.requireNonNull(registryName));
    }

    @Nullable
    public HeavySiftable getSiftable(ItemStack itemStack) {
        final ResourceLocation registryName = itemStack.getItem().getRegistryName();
        return getSiftable(Objects.requireNonNull(registryName));
    }

    @Nullable
    private HeavySiftable getSiftable(ResourceLocation registryName) {
        HeavySiftable customEntry = entries.get(registryName);
        if (customEntry != null) {
            return customEntry;
        }

        HeavySiftable generatedEntry = generatedEntriesCache.get(registryName);
        if (generatedEntry == null) {
            GeneratedHeavySiftable generatedHeavySiftable = generatedEntries.get(registryName);
            if (generatedHeavySiftable != null) {
                LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(generatedHeavySiftable.getSource(), generatedHeavySiftable.getTimes());
                HeavySiftable generatedSiftable = new HeavySiftable();
                generatedSiftable.setSource(registryName);
                generatedSiftable.setLootTable(new LootTableProvider(lootTable));
                generatedEntriesCache.put(registryName, generatedSiftable);
            }
        }

        return generatedEntry;
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
        generatedEntries.clear();
        generatedEntriesCache.clear();
    }

    @Override
    protected void loadRegistry(HeavySieveRegistryData data) {
        if (data.getGeneratedEntries() != null) {
            this.generatedEntries.putAll(data.getGeneratedEntries());
        }
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
