package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class HeavySieveRegistry {

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

    private static boolean testGeneratedRecipe(ItemStack itemStack, GeneratedHeavySieveRecipe generatedRecipe, BlockState sieve, SieveMeshRegistryEntry sieveMesh) {
        Block sourceBlock = ForgeRegistries.BLOCKS.getValue(generatedRecipe.getSource());
        return generatedRecipe.getInput().test(itemStack) && ExNihilo.isSiftableWithMesh(sieve, new ItemStack(sourceBlock), sieveMesh);
    }

    public static List<ItemStack> rollSieveRewards(LootContext context, BlockState sieve, SieveMeshRegistryEntry mesh, ItemStack itemStack) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.getValue(BlockStateProperties.WATERLOGGED);
        RecipeManager recipeManager = context.getLevel().getRecipeManager();
        List<HeavySieveRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.heavySieveRecipeType);
        List<ItemStack> results = new ArrayList<>();
        for (HeavySieveRecipe recipe : recipes) {
            if (testRecipe(mesh, itemStack, waterlogged, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId(), context);
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        List<GeneratedHeavySieveRecipe> generatedRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (GeneratedHeavySieveRecipe generatedRecipe : generatedRecipes) {
            if (testGeneratedRecipe(itemStack, generatedRecipe, sieve, mesh)) {
                int rolls = getGeneratedRollCount(generatedRecipe);
                ItemLike source = ForgeRegistries.ITEMS.getValue(generatedRecipe.getSource());
                LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(sieve, source, rolls, mesh);
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        return results;
    }

    public static Integer getGeneratedRollCount(GeneratedHeavySieveRecipe generatedRecipe) {
        return generatedRecipe.getRolls() != null ? generatedRecipe.getRolls() : ExCompressumConfig.getActive().general.heavySieveDefaultRolls;
    }

    public boolean isSiftable(Level level, BlockState sieve, ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.getValue(BlockStateProperties.WATERLOGGED);
        RecipeManager recipeManager = level.getRecipeManager();
        List<HeavySieveRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.heavySieveRecipeType);
        for (HeavySieveRecipe recipe : recipes) {
            if (testRecipe(sieveMesh, itemStack, waterlogged, recipe)) {
                return true;
            }
        }

        List<GeneratedHeavySieveRecipe> generatedRecipes = recipeManager.getAllRecipesFor(ModRecipeTypes.generatedHeavySieveRecipeType);
        for (GeneratedHeavySieveRecipe recipe : generatedRecipes) {
            if (testGeneratedRecipe(itemStack, recipe, sieve, sieveMesh)) {
                return true;
            }
        }

        return false;
    }

}
