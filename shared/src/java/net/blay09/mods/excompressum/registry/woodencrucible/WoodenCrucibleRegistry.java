package net.blay09.mods.excompressum.registry.woodencrucible;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class WoodenCrucibleRegistry {

    @Nullable
    public WoodenCrucibleRecipe getRecipe(Level level, ItemStack itemStack) {
        RecipeManager recipeManager = level.getRecipeManager();
        List<WoodenCrucibleRecipe> recipes = recipeManager.getAllRecipesFor(WoodenCrucibleRecipe.TYPE);
        for (WoodenCrucibleRecipe recipe : recipes) {
            if (recipe.getInput().test(itemStack)) {
                return recipe;
            }
        }
        return null;
    }

}
