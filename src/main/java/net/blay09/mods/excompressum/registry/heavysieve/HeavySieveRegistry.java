package net.blay09.mods.excompressum.registry.heavysieve;

import com.google.common.collect.Sets;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.LootTableProvider;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.registry.heavysieve.newregistry.HeavySieveRecipe;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameter;
import net.minecraft.loot.LootParameterSet;
import net.minecraft.loot.LootTable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;
import novamachina.exnihilosequentia.api.crafting.sieve.SieveRecipe;

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

    @Deprecated
    public Collection<HeavySiftable> getEntries() {
        return siftables;
    }

    @Deprecated
    public Map<ResourceLocation, GeneratedHeavySiftable> getGeneratedEntries() {
        return generatedEntries;
    }

    @Deprecated
    public Collection<HeavySiftable> getGeneratedEntriesCache() {
        return generatedEntriesCache.values();
    }

    @Deprecated
    public boolean isSiftable(BlockState sieveState, ItemStack itemStack, @Nullable SieveMeshRegistryEntry mesh) {
        return getSiftable(sieveState, itemStack, mesh) != null;
    }

    @Nullable
    @Deprecated
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
                HeavySiftable generatedSiftable = generateSiftable(sieveState, mesh, waterlogged, itemRegistryName, generatedHeavySiftable);
                generatedEntriesCache.put(heavySiftableKey, generatedSiftable);
            }
        }

        return generatedEntry;
    }

    @Deprecated
    public static HeavySiftable generateSiftable(BlockState sieveState, @Nullable SieveMeshRegistryEntry mesh, boolean waterlogged, ResourceLocation itemRegistryName, GeneratedHeavySiftable generatedHeavySiftable) {
        IItemProvider source = ForgeRegistries.ITEMS.getValue(generatedHeavySiftable.getSource());
        IItemProvider compressedSource = ForgeRegistries.ITEMS.getValue(itemRegistryName);
        int times = generatedHeavySiftable.getTimes() != null ? generatedHeavySiftable.getTimes() : ExCompressumConfig.COMMON.heavySieveDefaultRolls.get();
        LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(sieveState, source, times, mesh);
        HeavySiftable generatedSiftable = new HeavySiftable();
        generatedSiftable.setId(new ResourceLocation(ExCompressum.MOD_ID, itemRegistryName.getPath()));
        generatedSiftable.setSource(Ingredient.fromItems(compressedSource));
        generatedSiftable.setWaterlogged(waterlogged);
        generatedSiftable.setLootTable(new LootTableProvider(lootTable));
        generatedSiftable.setMeshes(mesh != null ? Sets.newHashSet(mesh.getMeshType()) : Collections.emptySet());
        return generatedSiftable;
    }

    @Deprecated
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
    public static LootContext buildLootContext(ServerWorld world, ItemStack itemStack, Random random) {
        return new LootContext.Builder(world)
                .withRandom(random)
                .withParameter(SOURCE_STACK, itemStack)
                .build(new LootParameterSet.Builder().required(SOURCE_STACK).build());
    }

    private static boolean testRecipe(SieveMeshRegistryEntry mesh, ItemStack itemStack, boolean waterlogged, HeavySieveRecipe recipe) {
        if (recipe.isWaterlogged() != waterlogged) {
            return false;
        }

        if (recipe.getMinimumMesh() != null) {
            SieveMeshRegistryEntry minimumMesh = SieveMeshRegistry.getEntry(recipe.getMinimumMesh());
            if (mesh.getMeshLevel() < minimumMesh.getMeshLevel()) {
                return false;
            }
        }

        if (recipe.getMeshes() != null && !recipe.getMeshes().contains(mesh.getMeshType())) {
            return false;
        }

        return recipe.getInput().test(itemStack);
    }

    public static List<ItemStack> rollSieveRewards(LootContext context, BlockState sieve, SieveMeshRegistryEntry mesh, ItemStack itemStack) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.get(BlockStateProperties.WATERLOGGED);
        RecipeManager recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
        List<HeavySieveRecipe> recipes = recipeManager.getRecipesForType(HeavySieveRecipe.TYPE);
        List<ItemStack> results = new ArrayList<>();
        for (HeavySieveRecipe recipe : recipes) {
            if (testRecipe(mesh, itemStack, waterlogged, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId().toString(), context);
                if (lootTable != null) {
                    results.addAll(lootTable.generate(context));
                }
            }
        }

        return results;
    }

    @Deprecated
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
