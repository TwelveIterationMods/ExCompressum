package net.blay09.mods.excompressum.registry.compressor;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompressedRecipeRegistry implements ResourceManagerReloadListener {

    private final List<CompressedRecipe> recipesSmall = new ArrayList<>();
    private final List<CompressedRecipe> recipes = new ArrayList<>();

    private final Map<ResourceLocation, CompressedRecipe> cachedResults = new HashMap<>();
    private final RecipeManager recipeManager;

    private boolean recipesLoaded;

    public CompressedRecipeRegistry(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        recipesLoaded = false;
    }

    public void reloadRecipes() {
        cachedResults.clear();
        recipesSmall.clear();
        recipes.clear();

        for (Recipe<?> recipe : recipeManager.getAllRecipesFor(RecipeType.CRAFTING)) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            int count = ingredients.size();
            if (count == 4 || count == 9) {
                Ingredient first = ingredients.get(0);
                boolean passes = true;
                for (int i = 1; i < count; i++) {
                    Ingredient other = ingredients.get(i);
                    boolean passesInner = false;
                    for (ItemStack itemStack : other.getItems()) {
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
                if (count == 4 && recipe.canCraftInDimensions(2, 2)) {
                    if (passes) {
                        recipesSmall.add(new CompressedRecipe(first, 4, recipe.getResultItem().copy()));
                    }
                } else if (count == 9 && recipe.canCraftInDimensions(3, 3)) {
                    if (passes) {
                        recipes.add(new CompressedRecipe(first, 9, recipe.getResultItem().copy()));
                    }
                }
            }
        }

        recipesLoaded = true;
    }

    @Nullable
    public CompressedRecipe getRecipe(ItemStack itemStack) {
        if (!recipesLoaded) {
            reloadRecipes();
        }

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
