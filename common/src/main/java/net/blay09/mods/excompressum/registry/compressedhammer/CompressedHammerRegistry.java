package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.*;

public class CompressedHammerRegistry {

    public static List<ItemStack> rollHammerRewards(Level level, LootContext context, ItemStack itemStack) {
        final var recipeManager = level.getRecipeManager();
        final var recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.compressedHammerRecipeType);
        List<ItemStack> results = new ArrayList<>();
        for (final var recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                final var lootTable = recipe.getLootTable();
                if (lootTable != null) {
                    lootTable.getRandomItems(context, results::add);
                }
            }
        }

        results.addAll(ExNihilo.getInstance().rollCompressedHammerRewards(level, context, itemStack));

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, CompressedHammerRecipe recipe) {
        return recipe.getIngredient().test(itemStack);
    }

    public boolean isHammerable(Level level, ItemStack itemStack) {
        return isHammerable(level.getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        final var recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.compressedHammerRecipeType);
        for (final var recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return ExNihilo.getInstance().isHammerableCompressed(itemStack);
    }

}
