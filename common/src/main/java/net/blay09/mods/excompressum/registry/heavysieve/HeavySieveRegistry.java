package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

public class HeavySieveRegistry {

    private static boolean testRecipe(SieveMeshRegistryEntry mesh, ItemStack itemStack, boolean waterlogged, HeavySieveRecipe recipe) {
        if (recipe.isWaterlogged() != waterlogged) {
            return false;
        }

        if (recipe.getMinimumMesh() != null) {
            SieveMeshRegistryEntry minimumMesh = SieveMeshRegistry.getEntry(recipe.getMinimumMesh());
            if (minimumMesh != null && mesh.getMeshLevel() < minimumMesh.getMeshLevel()) {
                return false;
            }
        }

        if (recipe.getMeshes() != null && !recipe.getMeshes().contains(mesh.getMeshType())) {
            return false;
        }

        return recipe.getIngredient().test(itemStack);
    }

    private static boolean testGeneratedRecipe(Level level, ItemStack itemStack, GeneratedHeavySieveRecipe generatedRecipe, BlockState sieve, SieveMeshRegistryEntry sieveMesh) {
        Block sourceBlock = Balm.getRegistries().getBlock(generatedRecipe.getSource());
        return generatedRecipe.getInput().test(itemStack) && ExNihilo.isSiftableWithMesh(level, sieve, new ItemStack(sourceBlock), sieveMesh);
    }

    public static List<ItemStack> rollSieveRewards(Level level, LootContext context, BlockState sieve, SieveMeshRegistryEntry mesh, ItemStack itemStack) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.getValue(BlockStateProperties.WATERLOGGED);
        final var recipeManager = context.getLevel().getRecipeManager();
        final var recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.heavySieveRecipeType);
        List<ItemStack> results = new ArrayList<>();
        for (final var recipe : recipes) {
            if (testRecipe(mesh, itemStack, waterlogged, recipe)) {
                LootTable lootTable = recipe.getLootTable();
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        final var generatedRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (final var generatedRecipe : generatedRecipes) {
            if (testGeneratedRecipe(level, itemStack, generatedRecipe, sieve, mesh)) {
                int rolls = getGeneratedRollCount(generatedRecipe);
                ItemLike source = Balm.getRegistries().getItem(generatedRecipe.getSource());
                LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(level, sieve, source, rolls, mesh);
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        final var inputState = StupidUtils.getStateFromItemStack(itemStack);
        results.addAll(ExNihilo.getInstance().rollHeavySieveRewards(level, sieve, inputState, mesh, context.getLuck(), level.random));

        return results;
    }

    public static Integer getGeneratedRollCount(GeneratedHeavySieveRecipe generatedRecipe) {
        return generatedRecipe.getRolls() != null ? generatedRecipe.getRolls() : ExCompressumConfig.getActive().general.heavySieveDefaultRolls;
    }

    public boolean isSiftable(Level level, BlockState sieve, ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.getValue(BlockStateProperties.WATERLOGGED);
        final var recipeManager = level.getRecipeManager();
        final var recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.heavySieveRecipeType);
        for (final var recipe : recipes) {
            if (testRecipe(sieveMesh, itemStack, waterlogged, recipe)) {
                return true;
            }
        }

        final var generatedRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (final var recipe : generatedRecipes) {
            if (testGeneratedRecipe(level, itemStack, recipe, sieve, sieveMesh)) {
                return true;
            }
        }

        final var state = StupidUtils.getStateFromItemStack(itemStack);
        return ExNihilo.getInstance().isHeavySiftableWithMesh(sieve, state, sieveMesh);
    }

}
