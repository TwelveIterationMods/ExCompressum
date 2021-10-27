package net.blay09.mods.excompressum.registry.chickenstick;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class ChickenStickRegistry {

    public static List<ItemStack> rollHammerRewards(Level level, LootContext context, ItemStack itemStack) {
        RecipeManager recipeManager = level.getRecipeManager();
        List<ChickenStickRecipe> recipes = recipeManager.getAllRecipesFor(ChickenStickRecipe.TYPE);
        List<ItemStack> results = new ArrayList<>();
        for (ChickenStickRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId(), context);
                if (lootTable != null) {
                    results.addAll(lootTable.getRandomItems(context));
                }
            }
        }

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, ChickenStickRecipe recipe) {
        return recipe.getInput().test(itemStack);
    }

    public boolean isHammerable(Level level, ItemStack itemStack) {
        return isHammerable(level.getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        List<ChickenStickRecipe> recipes = recipeManager.getAllRecipesFor(ChickenStickRecipe.TYPE);
        for (ChickenStickRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return false;
    }

}
