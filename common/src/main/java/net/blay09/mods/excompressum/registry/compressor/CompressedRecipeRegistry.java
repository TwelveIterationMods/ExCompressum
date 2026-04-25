package net.blay09.mods.excompressum.registry.compressor;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CompressedRecipeRegistry {

    private List<CompressedRecipe> recipesSmall = new ArrayList<>();
    private List<CompressedRecipe> recipes = new ArrayList<>();

    private final Map<ResourceLocation, CompressedRecipe> cachedResults = new ConcurrentHashMap<>();

    public CompressedRecipeRegistry() {
    }

    public void reloadRecipes(RecipeManager recipeManager, RegistryAccess registryAccess) {
        final var recipesSmall = new ArrayList<CompressedRecipe>();
        final var recipes = new ArrayList<CompressedRecipe>();

        for (final var recipeHolder : recipeManager.getAllRecipesFor(RecipeType.CRAFTING)) {
            final var recipe = recipeHolder.value();
            final var ingredients = recipe.getIngredients();
            int count = ingredients.size();
            if (count == 4 || count == 9) {
                final var first = ingredients.getFirst();
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
                        recipesSmall.add(new CompressedRecipe(first, 4, recipe.getResultItem(registryAccess).copy()));
                    }
                } else if (count == 9 && recipe.canCraftInDimensions(3, 3)) {
                    if (passes) {
                        recipes.add(new CompressedRecipe(first, 9, recipe.getResultItem(registryAccess).copy()));
                    }
                }
            }
        }

        this.recipesSmall = recipesSmall;
        this.recipes = recipes;
        cachedResults.clear();
    }

    @Nullable
    public CompressedRecipe getRecipe(ItemStack itemStack) {
        if (!itemStack.getComponentsPatch().isEmpty()) {
            return null;
        }

        final ResourceLocation registryName = Balm.getRegistries().getKey(itemStack.getItem());
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
