package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.mixin.RecipeManagerAccessor;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;

import org.jspecify.annotations.Nullable;

public class WoodenCrucibleRegistry {

    @Nullable
    public WoodenCrucibleRecipe getRecipe(ServerLevel level, ItemStack itemStack) {
        final var recipeManager = level.getServer().getRecipeManager();
        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        final var recipes = recipeMap.byType(ModRecipeTypes.woodenCrucible.type());
        for (final var recipeHolder : recipes) {
            final var recipe = recipeHolder.value();
            if (recipe.getIngredient().test(itemStack)) {
                return recipe;
            }
        }
        return null;
    }

}
