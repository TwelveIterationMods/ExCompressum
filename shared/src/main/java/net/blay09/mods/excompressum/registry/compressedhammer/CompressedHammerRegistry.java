package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

public class CompressedHammerRegistry {

    public static List<ItemStack> rollHammerRewards(Level level, LootContext context, ItemStack itemStack) {
        RecipeManager recipeManager = level.getRecipeManager();
        List<CompressedHammerRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.compressedHammerRecipeType);
        List<ItemStack> results = new ArrayList<>();
        for (CompressedHammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId(), context);
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, CompressedHammerRecipe recipe) {
        return recipe.getInput().test(itemStack);
    }

    public boolean isHammerable(Level level, ItemStack itemStack) {
        return isHammerable(level.getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        List<CompressedHammerRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.compressedHammerRecipeType);
        for (CompressedHammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return false;
    }

}
