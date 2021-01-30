package net.blay09.mods.excompressum.newregistry.compressedhammer;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.*;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class CompressedHammerRegistry {

    public static List<ItemStack> rollHammerRewards(LootContext context, ItemStack itemStack) {
        RecipeManager recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
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

    public boolean isHammerable(ItemStack itemStack) {
        RecipeManager recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
        List<CompressedHammerRecipe> recipes = recipeManager.getRecipesForType(CompressedHammerRecipe.TYPE);
        for (CompressedHammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return false;
    }

}
