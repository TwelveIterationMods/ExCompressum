package net.blay09.mods.excompressum.registry.compressedhammer;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.*;
import net.minecraft.world.World;

import java.util.*;

public class CompressedHammerRegistry {

    public static List<ItemStack> rollHammerRewards(World world, LootContext context, ItemStack itemStack) {
        RecipeManager recipeManager = world.getRecipeManager();
        List<CompressedHammerRecipe> recipes = recipeManager.getRecipesForType(CompressedHammerRecipe.TYPE);
        List<ItemStack> results = new ArrayList<>();
        for (CompressedHammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId(), context);
                if (lootTable != null) {
                    results.addAll(lootTable.generate(context));
                }
            }
        }

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, CompressedHammerRecipe recipe) {
        return recipe.getInput().test(itemStack);
    }

    public boolean isHammerable(World world, ItemStack itemStack) {
        return isHammerable(world.getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        List<CompressedHammerRecipe> recipes = recipeManager.getRecipesForType(CompressedHammerRecipe.TYPE);
        for (CompressedHammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return false;
    }

}
