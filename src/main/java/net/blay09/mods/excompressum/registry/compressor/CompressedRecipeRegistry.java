package net.blay09.mods.excompressum.registry.compressor;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompressedRecipeRegistry implements IResourceManagerReloadListener {

    private final List<CompressedRecipe> recipesSmall = new ArrayList<>();
    private final List<CompressedRecipe> recipes = new ArrayList<>();

    private final Map<ResourceLocation, CompressedRecipe> cachedResults = new HashMap<>();
    private final DataPackRegistries dataPackRegistries;

    public CompressedRecipeRegistry(DataPackRegistries dataPackRegistries) {
        this.dataPackRegistries = dataPackRegistries;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        cachedResults.clear();
        recipesSmall.clear();
        recipes.clear();

        final RecipeManager recipeManager = dataPackRegistries.getRecipeManager();
        for (IRecipe<?> recipe : recipeManager.getRecipes()) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            int count = ingredients.size();
            if (count == 4 || count == 9) {
                Ingredient first = ingredients.get(0);
                boolean passes = true;
                for (int i = 1; i < count; i++) {
                    Ingredient other = ingredients.get(i);
                    boolean passesInner = false;
                    for (ItemStack itemStack : other.getMatchingStacks()) {
                        if (first.test(itemStack)) {
                            passesInner = true;
                            break;
                        }
                    }
                    if (!passesInner) {
                        passes = false;
                        break;
                    }
                }
                if (count == 4 && recipe.canFit(2, 2)) {
                    if (passes) {
                        recipesSmall.add(new CompressedRecipe(first, 4, recipe.getRecipeOutput().copy()));
                    }
                } else if (count == 9 && recipe.canFit(3, 3)) {
                    if (passes) {
                        recipes.add(new CompressedRecipe(first, 9, recipe.getRecipeOutput().copy()));
                    }
                }
            }
        }
    }

    @Nullable
    public CompressedRecipe getRecipe(ItemStack itemStack) {
        if (itemStack.getTag() != null) {
            return null;
        }

        final ResourceLocation registryName = itemStack.getItem().getRegistryName();
        CompressedRecipe foundRecipe = cachedResults.get(registryName);
        if (foundRecipe != null) {
            return foundRecipe;
        }

        for (CompressedRecipe recipe : recipes) {
            if (recipe.getIngredient().test(itemStack)) {
                cachedResults.put(registryName, recipe);
                return recipe;
            }
        }

        for (CompressedRecipe recipe : recipesSmall) {
            if (recipe.getIngredient().test(itemStack)) {
                cachedResults.put(registryName, recipe);
                return recipe;
            }
        }

        cachedResults.put(registryName, null);
        return null;
    }

}
