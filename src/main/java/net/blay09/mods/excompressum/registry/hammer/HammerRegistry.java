package net.blay09.mods.excompressum.registry.hammer;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.*;

public class HammerRegistry {

    public static List<ItemStack> rollHammerRewards(LootContext context, ItemStack itemStack) {
        RecipeManager recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
        List<HammerRecipe> recipes = recipeManager.getRecipesForType(HammerRecipe.TYPE);
        List<ItemStack> results = new ArrayList<>();
        for (HammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId(), context);
                if (lootTable != null) {
                    results.addAll(lootTable.generate(context));
                }
            }
        }

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, HammerRecipe recipe) {
        return recipe.getInput().test(itemStack);
    }

    public boolean isHammerable(ItemStack itemStack) {
        RecipeManager recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
        List<HammerRecipe> recipes = recipeManager.getRecipesForType(HammerRecipe.TYPE);
        for (HammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return false;
    }

}
