package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.mixin.RecipeManagerAccessor;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.*;

public class CompressedHammerRegistry {

    public static List<ItemStack> rollHammerRewards(ServerLevel level, LootContext context, ItemStack itemStack) {
        final var recipeManager = level.getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.compressedHammer.type());
        List<ItemStack> results = new ArrayList<>();
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
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

    public boolean isHammerable(ServerLevel level, ItemStack itemStack) {
        return isHammerable(level.getServer().getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.compressedHammer.type());
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return ExNihilo.getInstance().isHammerableCompressed(itemStack);
    }

}
