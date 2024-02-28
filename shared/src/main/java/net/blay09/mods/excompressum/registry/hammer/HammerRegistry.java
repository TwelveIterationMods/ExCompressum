package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

public class HammerRegistry {

    public static List<ItemStack> rollHammerRewards(LootContext context, ItemStack itemStack) {
        RecipeManager recipeManager = context.getLevel().getRecipeManager();
        List<HammerRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.hammerRecipeType);
        List<ItemStack> results = new ArrayList<>();
        for (HammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId(), context);
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, HammerRecipe recipe) {
        return recipe.getInput().test(itemStack);
    }

    public boolean isHammerable(Level level, ItemStack itemStack) {
        return isHammerable(level.getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        List<HammerRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.hammerRecipeType);
        for (HammerRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return false;
    }

}
