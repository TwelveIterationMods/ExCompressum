package net.blay09.mods.excompressum.newregistry.woodencrucible;

import net.blay09.mods.excompressum.newregistry.hammer.HammerRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.List;

public class WoodenCrucibleRegistry {

    @Nullable
    public WoodenCrucibleRecipe getRecipe(ItemStack itemStack) {
        RecipeManager recipeManager = ServerLifecycleHooks.getCurrentServer().getRecipeManager();
        List<WoodenCrucibleRecipe> recipes = recipeManager.getRecipesForType(WoodenCrucibleRecipe.TYPE);
        for (WoodenCrucibleRecipe recipe : recipes) {
            if (recipe.getInput().test(itemStack)) {
                return recipe;
            }
        }
        return null;
    }

}
