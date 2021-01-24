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
import net.minecraft.loot.LootTable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

public class HeavySieveRegistry {

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

    public boolean isSiftable(BlockState sieve, ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.get(BlockStateProperties.WATERLOGGED);
        RecipeManager recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
        List<HeavySieveRecipe> recipes = recipeManager.getRecipesForType(HeavySieveRecipe.TYPE);
        for (HeavySieveRecipe recipe : recipes) {
            if (testRecipe(sieveMesh, itemStack, waterlogged, recipe)) {
                return true;
            }
        }

        return false;
    }
}
