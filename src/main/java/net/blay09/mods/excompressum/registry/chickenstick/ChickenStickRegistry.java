package net.blay09.mods.excompressum.registry.chickenstick;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.world.World;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.List;

public class ChickenStickRegistry {

    public static List<ItemStack> rollHammerRewards(World world, LootContext context, ItemStack itemStack) {
        RecipeManager recipeManager = world.getRecipeManager();
        List<ChickenStickRecipe> recipes = recipeManager.getRecipesForType(ChickenStickRecipe.TYPE);
        List<ItemStack> results = new ArrayList<>();
        for (ChickenStickRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                LootTable lootTable = recipe.getLootTable().getLootTable(recipe.getId(), context);
                if (lootTable != null) {
                    results.addAll(lootTable.generate(context));
                }
            }
        }

        return results;
    }

    private static boolean testRecipe(ItemStack itemStack, ChickenStickRecipe recipe) {
        return recipe.getInput().test(itemStack);
    }

    public boolean isHammerable(World world, ItemStack itemStack) {
        return isHammerable(world.getRecipeManager(), itemStack);
    }

    public boolean isHammerable(RecipeManager recipeManager, ItemStack itemStack) {
        List<ChickenStickRecipe> recipes = recipeManager.getRecipesForType(ChickenStickRecipe.TYPE);
        for (ChickenStickRecipe recipe : recipes) {
            if (testRecipe(itemStack, recipe)) {
                return true;
            }
        }

        return false;
    }

}
