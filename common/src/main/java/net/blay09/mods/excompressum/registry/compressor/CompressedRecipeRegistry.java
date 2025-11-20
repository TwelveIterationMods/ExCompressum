package net.blay09.mods.excompressum.registry.compressor;

import net.blay09.mods.excompressum.mixin.RecipeManagerAccessor;
import net.blay09.mods.excompressum.mixin.ShapedRecipeAccessor;
import net.blay09.mods.excompressum.mixin.ShapelessRecipeAccessor;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompressedRecipeRegistry {

    private final Map<Identifier, CompressedRecipe> recipesById = new HashMap<>();
    private final List<CompressedRecipe> recipesSmall = new ArrayList<>();
    private final List<CompressedRecipe> recipes = new ArrayList<>();

    private final Map<Identifier, CompressedRecipe> cacheByItemId = new HashMap<>();

    public CompressedRecipeRegistry() {
    }

    public void reloadRecipes(RecipeManager recipeManager, RegistryAccess registryAccess) {
        recipesById.clear();
        cacheByItemId.clear();
        recipesSmall.clear();
        recipes.clear();

        final var recipeMap = ((RecipeManagerAccessor) recipeManager).getRecipes();
        for (final var recipeHolder : recipeMap.byType(RecipeType.CRAFTING)) {
            final var recipe = recipeHolder.value();
            if (recipe instanceof ShapedRecipe shapedRecipe) {
                final var ingredients = shapedRecipe.getIngredients();
                int count = ingredients.size();
                if (count == 4 || count == 9) {
                    final var optionalFirst = ingredients.getFirst();
                    if (optionalFirst.isEmpty()) {
                        continue;
                    }
                    final var first = optionalFirst.get();

                    boolean passes = true;
                    for (int i = 1; i < count; i++) {
                        boolean passesInner = false;
                        final var optionalOther = ingredients.get(i);
                        if (optionalOther.isPresent()) {
                            final var other = optionalOther.get();
                            final var items = other.items().toList();
                            for (final var itemHolder : items) {
                                if (first.test(new ItemStack(itemHolder))) {
                                    passesInner = true;
                                    break;
                                }
                            }
                        }
                        if (!passesInner) {
                            passes = false;
                            break;
                        }
                    }

                    final var result = ((ShapedRecipeAccessor) shapedRecipe).getResult();
                    if (count == 4 && shapedRecipe.getWidth() == 2 && shapedRecipe.getHeight() == 2) {
                        if (passes) {
                            recipesSmall.add(new CompressedRecipe(recipeHolder.id().identifier(), first, 4, result.copy()));
                        }
                    } else if (count == 9 && shapedRecipe.getWidth() == 3 && shapedRecipe.getHeight() == 3) {
                        if (passes) {
                            recipes.add(new CompressedRecipe(recipeHolder.id().identifier(), first, 9, result.copy()));
                        }
                    }
                }
            } else if (recipe instanceof ShapelessRecipe shapelessRecipe) {
                final var ingredients = ((ShapelessRecipeAccessor) shapelessRecipe).getIngredients();
                int count = ingredients.size();
                if (count == 4 || count == 9) {
                    final var first = ingredients.getFirst();
                    boolean passes = true;
                    for (int i = 1; i < count; i++) {
                        Ingredient other = ingredients.get(i);
                        boolean passesInner = false;
                        final var items = other.items().toList();
                        for (final var itemHolder : items) {
                            if (first.test(new ItemStack(itemHolder))) {
                                passesInner = true;
                                break;
                            }
                        }
                        if (!passesInner) {
                            passes = false;
                            break;
                        }
                    }
                    final var result = ((ShapelessRecipeAccessor) shapelessRecipe).getResult();
                    if (count == 4) {
                        if (passes) {
                            recipesSmall.add(new CompressedRecipe(recipeHolder.id().identifier(), first, 4, result.copy()));
                        }
                    } else {
                        if (passes) {
                            recipes.add(new CompressedRecipe(recipeHolder.id().identifier(), first, 9, result.copy()));
                        }
                    }
                }
            }
        }
    }

    @Nullable
    public CompressedRecipe getRecipe(ItemStack itemStack) {
        if (!itemStack.getComponentsPatch().isEmpty()) {
            return null;
        }

        final Identifier registryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
        CompressedRecipe foundRecipe = cacheByItemId.get(registryName);
        if (foundRecipe != null) {
            return foundRecipe;
        }

        for (CompressedRecipe recipe : recipes) {
            if (recipe.ingredient().test(itemStack)) {
                cacheByItemId.put(registryName, recipe);
                return recipe;
            }
        }

        for (CompressedRecipe recipe : recipesSmall) {
            if (recipe.ingredient().test(itemStack)) {
                cacheByItemId.put(registryName, recipe);
                return recipe;
            }
        }

        cacheByItemId.put(registryName, null);
        return null;
    }

    public CompressedRecipe getRecipeById(Identifier id) {
        return recipesById.get(id);
    }
}
