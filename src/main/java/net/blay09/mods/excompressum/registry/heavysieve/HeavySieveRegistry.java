package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.*;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
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
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.get(BlockStateProperties.WATERLOGGED);
        RecipeManager recipeManager = context.getWorld().getRecipeManager();
        List<HeavySieveRecipe> recipes = recipeManager.getRecipesForType(HeavySieveRecipe.TYPE);
        List<ItemStack> results = new ArrayList<>();
        for (HeavySieveRecipe recipe : recipes) {
            if (testRecipe(mesh, itemStack, waterlogged, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId(), context);
                if (lootTable != null) {
                    results.addAll(lootTable.generate(context));
                }
            }
        }

        List<GeneratedHeavySieveRecipe> generatedRecipes = recipeManager.getRecipesForType(GeneratedHeavySieveRecipe.TYPE);
        for (GeneratedHeavySieveRecipe generatedRecipe : generatedRecipes) {
            if (testGeneratedRecipe(itemStack, generatedRecipe, sieve, mesh)) {
                int rolls = getGeneratedRollCount(generatedRecipe);
                IItemProvider source = ForgeRegistries.ITEMS.getValue(generatedRecipe.getSource());
                LootTable lootTable = ExNihilo.getInstance().generateHeavySieveLootTable(sieve, source, rolls, mesh);
                if (lootTable != null) {
                    results.addAll(lootTable.generate(context));
                }
            }
        }

        return results;
    }

    public static Integer getGeneratedRollCount(GeneratedHeavySieveRecipe generatedRecipe) {
        return generatedRecipe.getRolls() != null ? generatedRecipe.getRolls() : ExCompressumConfig.COMMON.heavySieveDefaultRolls.get();
    }

    public boolean isSiftable(World world, BlockState sieve, ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        boolean waterlogged = sieve.hasProperty(BlockStateProperties.WATERLOGGED) && sieve.get(BlockStateProperties.WATERLOGGED);
        RecipeManager recipeManager = world.getRecipeManager();
        List<HeavySieveRecipe> recipes = recipeManager.getRecipesForType(HeavySieveRecipe.TYPE);
        for (HeavySieveRecipe recipe : recipes) {
            if (testRecipe(sieveMesh, itemStack, waterlogged, recipe)) {
                return true;
            }
        }

        List<GeneratedHeavySieveRecipe> generatedRecipes = recipeManager.getRecipesForType(GeneratedHeavySieveRecipe.TYPE);
        for (GeneratedHeavySieveRecipe recipe : generatedRecipes) {
            if (testGeneratedRecipe(itemStack, recipe, sieve, sieveMesh)) {
                return true;
            }
        }

        return false;
    }

}
