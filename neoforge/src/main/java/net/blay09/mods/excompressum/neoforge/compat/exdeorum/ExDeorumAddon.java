package net.blay09.mods.excompressum.neoforge.compat.exdeorum;

import com.google.common.collect.ArrayListMultimap;
import it.unimi.dsi.fastutil.ints.IntList;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.recipe.SieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeImpl;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeImpl;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;

import java.util.*;

public class ExDeorumAddon implements ExNihiloProvider {

    private final Map<Item, CommonMeshType> itemToMeshType = new HashMap<>();

    public ExDeorumAddon() {
        ExNihilo.setInstance(this);

        final var stringMeshItem = findItem("string_mesh");
        if (!stringMeshItem.isEmpty()) {
            SieveMeshRegistryEntry stringMesh = new SieveMeshRegistryEntry(CommonMeshType.STRING, stringMeshItem, stringMeshItem.getItem());
            stringMesh.setModelName("string");
            SieveMeshRegistry.add(stringMesh);
            itemToMeshType.put(stringMeshItem.getItem(), CommonMeshType.STRING);
        }

        ItemStack flintMeshItem = findItem("flint_mesh");
        if (!flintMeshItem.isEmpty()) {
            SieveMeshRegistryEntry flintMesh = new SieveMeshRegistryEntry(CommonMeshType.FLINT, flintMeshItem, flintMeshItem.getItem());
            flintMesh.setModelName("flint");
            SieveMeshRegistry.add(flintMesh);
            itemToMeshType.put(flintMeshItem.getItem(), CommonMeshType.FLINT);
        }

        ItemStack ironMeshItem = findItem("iron_mesh");
        if (!ironMeshItem.isEmpty()) {
            SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(CommonMeshType.IRON, ironMeshItem, ironMeshItem.getItem());
            ironMesh.setHeavy(true);
            ironMesh.setModelName("iron");
            SieveMeshRegistry.add(ironMesh);
            SieveMeshRegistry.registerDefaults(ironMeshItem.getItem());
            itemToMeshType.put(ironMeshItem.getItem(), CommonMeshType.IRON);
        }

        ItemStack goldMeshItem = findItem("golden_mesh");
        if (!goldMeshItem.isEmpty()) {
            SieveMeshRegistryEntry goldMesh = new SieveMeshRegistryEntry(CommonMeshType.GOLD, goldMeshItem, goldMeshItem.getItem());
            goldMesh.setHeavy(true);
            goldMesh.setModelName("gold");
            SieveMeshRegistry.add(goldMesh);
            itemToMeshType.put(goldMeshItem.getItem(), CommonMeshType.GOLD);
        }

        ItemStack diamondMeshItem = findItem("diamond_mesh");
        if (!diamondMeshItem.isEmpty()) {
            SieveMeshRegistryEntry diamondMesh = new SieveMeshRegistryEntry(CommonMeshType.DIAMOND, diamondMeshItem, diamondMeshItem.getItem());
            diamondMesh.setHeavy(true);
            diamondMesh.setModelName("diamond");
            SieveMeshRegistry.add(diamondMesh);
            itemToMeshType.put(diamondMeshItem.getItem(), CommonMeshType.DIAMOND);
        }

        ItemStack netheriteMeshItem = findItem("netherite_mesh");
        if (!netheriteMeshItem.isEmpty()) {
            SieveMeshRegistryEntry mesh = new SieveMeshRegistryEntry(CommonMeshType.NETHERITE, netheriteMeshItem, netheriteMeshItem.getItem());
            mesh.setHeavy(true);
            mesh.setModelName("netherite");
            SieveMeshRegistry.add(mesh);
            itemToMeshType.put(netheriteMeshItem.getItem(), CommonMeshType.NETHERITE);
        }
    }

    private ItemStack findItem(String name) {
        ResourceLocation location = ResourceLocation.fromNamespaceAndPath(Compat.EX_DEORUM, name);
        Item item = Balm.getRegistries().getItem(location);
        return new ItemStack(item);
    }

    @Override
    public boolean isHammerable(Level level, BlockState state) {
        return RecipeUtil.getHammerRecipe(StupidUtils.getItemStackFromState(state).getItem()) != null;
    }

    @Override
    public boolean isHammerableCompressed(ItemStack itemStack) {
        return RecipeUtil.getCompressedHammerRecipe(itemStack.getItem()) != null;
    }

    @Override
    public List<ItemStack> rollHammerRewards(Level level, BlockState state, ItemStack toolItem, RandomSource rand) {
        List<ItemStack> drops = new ArrayList<>();
        final var recipe = RecipeUtil.getHammerRecipe(StupidUtils.getItemStackFromState(state).getItem());
        if (recipe != null) {
            drops.add(recipe.getResultItem(level.registryAccess()));
        }

        return drops;
    }

    @Override
    public boolean isSiftableWithMesh(Level level, BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh) {
        if (sieveMesh == null) {
            return false;
        }

        return !RecipeUtil.getSieveRecipes((Item) sieveMesh.getBackingMesh(), StupidUtils.getItemStackFromState(state)).isEmpty();
    }

    @Override
    public boolean isHeavySiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh) {
        if (sieveMesh == null) {
            return false;
        }

        return !RecipeUtil.getCompressedSieveRecipes((Item) sieveMesh.getBackingMesh(), StupidUtils.getItemStackFromState(state)).isEmpty();
    }

    @Override
    public Collection<ItemStack> rollSieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        final var sourceStack = StupidUtils.getItemStackFromState(state);
        final var recipes = RecipeUtil.getSieveRecipes((Item) sieveMesh.getBackingMesh(), sourceStack);
        List<ItemStack> list = new ArrayList<>();
        for (final var recipe : recipes) {
            LootContext lootContext = LootTableUtils.buildLootContext((ServerLevel) level, sourceStack);
            final var amount = recipe.resultAmount.getInt(lootContext);
            if (amount > 0) {
                list.add(recipe.getResultItem(level.registryAccess()));
            }
        }
        return list;
    }

    @Override
    public Collection<ItemStack> rollHeavySieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
        final var sourceStack = StupidUtils.getItemStackFromState(state);
        final var recipes = RecipeUtil.getCompressedSieveRecipes((Item) sieveMesh.getBackingMesh(), sourceStack);
        List<ItemStack> list = new ArrayList<>();
        for (final var recipe : recipes) {
            LootContext lootContext = LootTableUtils.buildLootContext((ServerLevel) level, sourceStack);
            final var amount = recipe.resultAmount.getInt(lootContext);
            if (amount > 0) {
                list.add(recipe.getResultItem(level.registryAccess()));
            }
        }
        return list;
    }

    @Override
    public Collection<ItemStack> rollCompressedHammerRewards(Level level, LootContext context, ItemStack itemStack) {
        final var recipe = RecipeUtil.getCompressedHammerRecipe(itemStack.getItem());
        if (recipe != null) {
            List<ItemStack> list = new ArrayList<>();
            LootContext lootContext = LootTableUtils.buildLootContext((ServerLevel) level, itemStack);
            final var amount = recipe.resultAmount.getInt(lootContext);
            if (amount > 0) {
                list.add(recipe.getResultItem(level.registryAccess()));
            }
            return list;
        }
        return Collections.emptyList();
    }

    @Override
    public List<ItemStack> rollCrookRewards(ServerLevel level, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, RandomSource rand) {
        final float fortune = getLuckFromTool(level, tool);
        final var recipes = RecipeUtil.getCrookRecipes(state);
        List<ItemStack> list = new ArrayList<>();
        for (final var recipe : recipes) {
            int rolls = Math.max(1, Mth.ceil(fortune / 3f));
            for (int i = 0; i < rolls; i++) {
                if (rand.nextFloat() < recipe.chance()) {
                    list.add(recipe.getResultItem(level.registryAccess()));
                }
            }
        }
        return list;
    }

    private float getLuckFromTool(Level level, ItemStack tool) {
        final var fortuneEnchantment = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.FORTUNE);
        return EnchantmentHelper.getItemEnchantmentLevel(fortuneEnchantment, tool);
    }

    @Override
    public LootTable generateHeavySieveLootTable(Level level, BlockState sieveState, ItemLike source, int times, SieveMeshRegistryEntry mesh) {
        return LootTable.EMPTY; // We don't generate heavy sieve loot because Deorum has its own compressed sieve recipes
    }

    @Override
    public boolean doMeshesHaveDurability() {
        return false;
    }

    @Override
    public int getMeshFortune(ItemStack meshStack) {
        return 0;
    }

    @Override
    public int getMeshEfficiency(ItemStack meshStack) {
        return 0;
    }

    @Override
    public boolean isCompressableOre(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean isHammerableOre(ItemStack itemStack) {
        return false;
    }

    @Override
    public List<HammerRecipe> getHammerRecipes() {
        List<HammerRecipe> result = new ArrayList<>();

        ArrayListMultimap<IntList, thedarkcolour.exdeorum.recipe.hammer.HammerRecipe> groupedRecipes = ArrayListMultimap.create();
        for (final var hammerRecipe : RecipeUtil.getCachedHammerRecipes()) {
            groupedRecipes.put(hammerRecipe.value().ingredient().getStackingIds(), hammerRecipe.value());
        }

        for (final var packedStacks : groupedRecipes.keySet()) {
            final var tableBuilder = LootTable.lootTable();
            for (final var hammerRecipe : groupedRecipes.get(packedStacks)) {
                final var poolBuilder = LootPool.lootPool();
                final var entryBuilder = buildLootEntry(hammerRecipe.result(), hammerRecipe.resultAmount);
                poolBuilder.add(entryBuilder);
                tableBuilder.withPool(poolBuilder);
            }

            final var firstRecipe = groupedRecipes.get(packedStacks).getFirst();
            final var input = firstRecipe.ingredient();
            final var lootTableProvider = tableBuilder.build();
            result.add(new HammerRecipeImpl(input, lootTableProvider));
        }

        return result;
    }

    private LootPoolSingletonContainer.Builder<?> buildLootEntry(ItemStack itemStack, NumberProvider amount) {
        return LootTableUtils.buildLootEntry(itemStack, amount);
    }

    @Override
    public List<CompressedHammerRecipe> getCompressedHammerRecipes() {
        List<CompressedHammerRecipe> result = new ArrayList<>();

        ArrayListMultimap<IntList, thedarkcolour.exdeorum.recipe.hammer.CompressedHammerRecipe> groupedRecipes = ArrayListMultimap.create();
        for (final var hammerRecipe : RecipeUtil.getCachedCompressedHammerRecipes()) {
            groupedRecipes.put(hammerRecipe.value().ingredient().getStackingIds(), hammerRecipe.value());
        }

        for (final var packedStacks : groupedRecipes.keySet()) {
            final var tableBuilder = LootTable.lootTable();
            for (final var hammerRecipe : groupedRecipes.get(packedStacks)) {
                final var poolBuilder = LootPool.lootPool();
                final var entryBuilder = buildLootEntry(hammerRecipe.result(), hammerRecipe.resultAmount);
                poolBuilder.add(entryBuilder);
                tableBuilder.withPool(poolBuilder);
            }

            final var firstRecipe = groupedRecipes.get(packedStacks).getFirst();
            final var input = firstRecipe.ingredient();
            final var lootTableProvider = tableBuilder.build();
            result.add(new CompressedHammerRecipeImpl(input, lootTableProvider));
        }

        return result;
    }

    @Override
    public List<SieveRecipe> getSieveRecipes() {
        List<SieveRecipe> result = new ArrayList<>();

        // final var recipeManager = ExCompressum.proxy.get().getRecipeManager(null);
        // ArrayListMultimap<Pair<IntList, CommonMeshType>, thedarkcolour.exdeorum.recipe.sieve.SieveRecipe> groupedRecipes = ArrayListMultimap.create();
        // final var sieveRecipes = recipeManager.getAllRecipesFor(ERecipeTypes.SIEVE.get());
        // for (final var recipe : sieveRecipes) {
        //     final var mesh = itemToMeshType.get(recipe.mesh);
        //     groupedRecipes.put(Pair.of(recipe.getIngredient().getStackingIds(), mesh), recipe);
        // }

        // for (final var packedStacks : groupedRecipes.keySet()) {
        //     final var tableBuilder = LootTable.lootTable();
        //     for (final var recipe : groupedRecipes.get(packedStacks)) {
        //         final var poolBuilder = LootPool.lootPool();
        //         final var entryBuilder = buildLootEntry(recipe.result, recipe.resultAmount);
        //         poolBuilder.add(entryBuilder);
        //         tableBuilder.withPool(poolBuilder);
        //     }

        //     final var firstRecipe = groupedRecipes.get(packedStacks).get(0);
        //     final var ingredient = firstRecipe.getIngredient();
        //     final var lootTable = tableBuilder.build();
        //     result.add(new SieveRecipeImpl(firstRecipe.getId(), ingredient, lootTable, false, packedStacks.getSecond(), null));
        // }

        return result;
    }

    @Override
    public List<HeavySieveRecipe> getHeavySieveRecipes() {
        List<HeavySieveRecipe> result = new ArrayList<>();

        // final var recipeManager = ExCompressum.proxy.get().getRecipeManager(null);
        // ArrayListMultimap<Pair<IntList, CommonMeshType>, CompressedSieveRecipe> groupedRecipes = ArrayListMultimap.create();
        // final var compressedSieveRecipes = recipeManager.getAllRecipesFor(ERecipeTypes.COMPRESSED_SIEVE.get());
        // for (final var recipe : compressedSieveRecipes) {
        //     final var mesh = itemToMeshType.get(recipe.mesh);
        //     groupedRecipes.put(Pair.of(recipe.getIngredient().getStackingIds(), mesh), recipe);
        // }

        // for (final var packedStacks : groupedRecipes.keySet()) {
        //     final var tableBuilder = LootTable.lootTable();
        //     for (final var recipe : groupedRecipes.get(packedStacks)) {
        //         final var poolBuilder = LootPool.lootPool();
        //         final var entryBuilder = buildLootEntry(recipe.result, recipe.resultAmount);
        //         poolBuilder.add(entryBuilder);
        //         tableBuilder.withPool(poolBuilder);
        //     }

        //     final var firstRecipe = groupedRecipes.get(packedStacks).get(0);
        //     final var ingredient = firstRecipe.getIngredient();
        //     final var lootTable = tableBuilder.build();
        //     result.add(new HeavySieveRecipeImpl(firstRecipe.getId(), ingredient, lootTable, false, packedStacks.getSecond(), null));
        // }

        return result;
    }
}
