package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;
import java.util.List;

public class WoodenCrucibleRegistry {

    @Nullable
    public WoodenCrucibleRecipe getRecipe(Level level, ItemStack itemStack) {
        RecipeManager recipeManager = level.getRecipeManager();
        List<WoodenCrucibleRecipe> recipes = recipeManager.getAllRecipesFor(ModRecipeTypes.woodenCrucibleRecipeType);
        for (WoodenCrucibleRecipe recipe : recipes) {
            if (recipe.getInput().test(itemStack)) {
                return recipe;
            }
        }
        return null;
    }

}
