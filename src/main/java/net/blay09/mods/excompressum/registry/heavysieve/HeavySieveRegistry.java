package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootTable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class HeavySieveRegistry extends GroupedRegistry<
        RegistryGroup,
        RegistryOverride,
        RegistryOverride,
        HeavySiftable,
        HeavySieveRegistryData> {

    private static final LootParameter<ItemStack> SOURCE_STACK = new LootParameter<>(new ResourceLocation(ExCompressum.MOD_ID, "source_stack"));

    private final List<HeavySiftable> siftables = new ArrayList<>();
    private final Map<ResourceLocation, GeneratedHeavySiftable> generatedEntries = new HashMap<>();
    private final Map<HeavySiftableKey, HeavySiftable> generatedEntriesCache = new HashMap<>();

    public HeavySieveRegistry() {
        super("HeavySieve");
    }

    public Collection<HeavySiftable> getEntries() {
        return siftables; // TODO generated ones?
    }

    public boolean isSiftable(BlockState sieveState, ItemStack itemStack, @Nullable SieveMeshRegistryEntry mesh) {
        return getSiftable(sieveState, itemStack, mesh) != null;
    }

    @Nullable
    public HeavySiftable getSiftable(BlockState sieveState, ItemStack itemStack, @Nullable SieveMeshRegistryEntry mesh) {
        boolean waterlogged = sieveState.hasProperty(BlockStateProperties.WATERLOGGED) && sieveState.get(BlockStateProperties.WATERLOGGED);
        HeavySiftable customEntry = siftables.stream().filter(it -> siftableMatches(it, itemStack, waterlogged, mesh)).findFirst().orElse(null);
        if (customEntry != null) {
            return customEntry;
        }

        ResourceLocation itemRegistryName = Objects.requireNonNull(itemStack.getItem().getRegistryName());
        HeavySiftableKey heavySiftableKey = new HeavySiftableKey(itemRegistryName, mesh, waterlogged);
        HeavySiftable generatedEntry = generatedEntriesCache.get(heavySiftableKey);
        if (generatedEntry == null) {
            GeneratedHeavySiftable generatedHeavySiftable = generatedEntries.get(itemRegistryName);
            if (generatedHeavySiftable != null) {
                IItemProvider source = ForgeRegistries.ITEMS.getValue(generatedHeavySiftable.getSource());
                int times = generatedHeavySiftable.getTimes() != null ? generatedHeavySiftable.getTimes() : ExCompressumConfig.COMMON.heavySieveDefaultRolls.get();
                LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(sieveState, source, times, mesh);
                HeavySiftable generatedSiftable = new HeavySiftable();
                generatedSiftable.setId(itemRegistryName.getPath());
                generatedSiftable.setSource(Ingredient.fromItems(source));
                generatedSiftable.setWaterlogged(waterlogged);
                generatedSiftable.setLootTable(new LootTableProvider(lootTable));
                generatedEntriesCache.put(heavySiftableKey, generatedSiftable);
            }
        }

        return generatedEntry;
    }

    private boolean siftableMatches(HeavySiftable siftable, ItemStack itemStack, boolean waterlogged, SieveMeshRegistryEntry mesh) {
        if (siftable.isWaterlogged() != waterlogged) {
            return false;
        }

        if (siftable.getMinimumMesh() != null) {
            SieveMeshRegistryEntry minimumMesh = SieveMeshRegistry.getEntry(siftable.getMinimumMesh());
            if (mesh.getMeshLevel() < minimumMesh.getMeshLevel()) {
                return false;
            }
        }

        if (siftable.getMeshes() != null && !siftable.getMeshes().contains(mesh.getMeshType())) {
            return false;
        }

        return siftable.getSource().test(itemStack);
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
        siftables.clear();
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
        siftables.add(entry);
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
