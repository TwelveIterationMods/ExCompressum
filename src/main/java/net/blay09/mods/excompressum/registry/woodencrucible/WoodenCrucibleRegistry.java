package net.blay09.mods.excompressum.registry.woodencrucible;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.List;

public class WoodenCrucibleRegistry {

    @Nullable
    public WoodenCrucibleRecipe getRecipe(World world, ItemStack itemStack) {
        RecipeManager recipeManager = world.getRecipeManager();
        List<WoodenCrucibleRecipe> recipes = recipeManager.getRecipesForType(WoodenCrucibleRecipe.TYPE);
        for (WoodenCrucibleRecipe recipe : recipes) {
            if (recipe.getInput().test(itemStack)) {
                return recipe;
            }
        }
        return null;
    }

}
