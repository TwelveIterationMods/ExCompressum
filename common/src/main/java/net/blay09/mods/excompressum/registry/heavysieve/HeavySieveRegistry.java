package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.mixin.RecipeManagerAccessor;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class HeavySieveRegistry {

    private static boolean testRecipe(SieveMeshRegistryEntry mesh, ItemStack itemStack, boolean waterlogged, HeavySieveRecipe recipe) {
        if (recipe.isWaterlogged() != waterlogged) {
            return false;
        }

        if (!recipe.getMeshes().isEmpty() && !recipe.getMeshes().contains(mesh.getMeshType())) {
            return false;
        }

        return recipe.getIngredient().test(itemStack);
    }

    private static boolean testGeneratedRecipe(Level level, ItemStack itemStack, GeneratedHeavySieveRecipe generatedRecipe, BlockState sieve, SieveMeshRegistryEntry sieveMesh) {
        Block sourceBlock = BuiltInRegistries.BLOCK.getValue(generatedRecipe.getSourceItem());
        return generatedRecipe.getIngredient().test(itemStack) && ExNihilo.isSiftableWithMesh(level, sieve, new ItemStack(sourceBlock), sieveMesh);
    }

    public static List<ItemStack> rollSieveRewards(Level level, LootContext context, BlockState sieve, SieveMeshRegistryEntry mesh, ItemStack itemStack) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.getValue(BlockStateProperties.WATERLOGGED);
        final var recipeManager = context.getLevel().getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.heavySieve.type());
        List<ItemStack> results = new ArrayList<>();
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (testRecipe(mesh, itemStack, waterlogged, recipe)) {
                LootTable lootTable = recipe.getLootTable();
                lootTable.getRandomItems(context, results::add);
            }
        }

        final var generatedRecipes = recipeMap.byType(ModRecipeTypes.generatedHeavySieve.type());
        for (final var recipeHolder : generatedRecipes) {
            final var recipe = recipeHolder.value();
            if (testGeneratedRecipe(level, itemStack, recipe, sieve, mesh)) {
                int rolls = getGeneratedRollCount(recipe);
                ItemLike source = BuiltInRegistries.ITEM.getValue(recipe.getSourceItem());
                LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(level, sieve, source, rolls, mesh);
                lootTable.getRandomItems(context, results::add);
            }
        }

        final var inputState = StupidUtils.getStateFromItemStack(itemStack);
        results.addAll(ExNihilo.getInstance().rollHeavySieveRewards(level, sieve, inputState, mesh, context.getLuck(), level.getRandom()));

        return results;
    }

    public static Integer getGeneratedRollCount(GeneratedHeavySieveRecipe generatedRecipe) {
        return generatedRecipe.getRolls() > 0 ? generatedRecipe.getRolls() : ExCompressumConfig.getActive().general.heavySieveDefaultRolls;
    }

    public boolean isSiftable(ServerLevel level, BlockState sieve, ItemStack itemStack, @Nullable SieveMeshRegistryEntry sieveMesh) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.getValue(BlockStateProperties.WATERLOGGED);
        final var recipeManager = level.getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.heavySieve.type());
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (testRecipe(sieveMesh, itemStack, waterlogged, recipe)) {
                return true;
            }
        }

        final var generatedRecipes = recipeMap.byType(ModRecipeTypes.generatedHeavySieve.type());
        for (final var recipeHolder : generatedRecipes) {
            final var recipe = recipeHolder.value();
            if (testGeneratedRecipe(level, itemStack, recipe, sieve, sieveMesh)) {
                return true;
            }
        }

        final var state = StupidUtils.getStateFromItemStack(itemStack);
        return ExNihilo.getInstance().isHeavySiftableWithMesh(sieve, state, sieveMesh);
    }

}
