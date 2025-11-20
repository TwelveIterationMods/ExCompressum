package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.mixin.RecipeManagerAccessor;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.*;

public class HammerRegistry {

    public static List<ItemStack> rollHammerRewards(LootContext context, ItemStack itemStack) {
        final var recipeManager = context.getLevel().getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.hammer.type());
        final var results = new ArrayList<ItemStack>();
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (testRecipe(itemStack, recipe)) {
                LootTable lootTable = recipe.getLootTable();
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, HammerRecipe recipe) {
        return recipe.getIngredient().test(itemStack);
    }

    public boolean isHammerable(ServerLevel level, ItemStack itemStack) {
        return isHammerable(level.getServer().getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.hammer.type());
        for (final var recipeHolder : recipes) {
            if (testRecipe(itemStack, recipeHolder.value())) {
                return true;
            }
        }

        return false;
    }

}
