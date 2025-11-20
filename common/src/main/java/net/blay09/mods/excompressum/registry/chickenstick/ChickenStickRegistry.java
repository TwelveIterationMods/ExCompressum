package net.blay09.mods.excompressum.registry.chickenstick;

import net.blay09.mods.excompressum.mixin.RecipeManagerAccessor;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class ChickenStickRegistry {

    public static List<ItemStack> rollHammerRewards(ServerLevel level, LootContext context, ItemStack itemStack) {
        final var recipeManager = level.getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.chickenStick.type());
        List<ItemStack> results = new ArrayList<>();
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

    private static boolean testRecipe(ItemStack itemStack, ChickenStickRecipe recipe) {
        return recipe.getIngredient().test(itemStack);
    }

    public boolean isHammerable(ServerLevel level, ItemStack itemStack) {
        return isHammerable(level.getServer().getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.chickenStick.type());
        for (final var recipeHolder : recipes) {
            if (testRecipe(itemStack, recipeHolder.value())) {
                return true;
            }
        }

        return false;
    }

}
