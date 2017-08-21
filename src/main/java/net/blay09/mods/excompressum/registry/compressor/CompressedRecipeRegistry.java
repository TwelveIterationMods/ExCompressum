package net.blay09.mods.excompressum.registry.compressor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.blay09.mods.excompressum.registry.RegistryKey;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class CompressedRecipeRegistry {

    private static final List<CompressedRecipe> recipesSmall = Lists.newArrayList();
    private static final List<CompressedRecipe> recipes = Lists.newArrayList();

    private static final Map<RegistryKey, CompressedRecipe> cachedResults = Maps.newHashMap();

    public static void reload() {
        cachedResults.clear();
        recipesSmall.clear();
        recipes.clear();
        for(IRecipe recipe : CraftingManager.REGISTRY) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            int count = ingredients.size();
            if(count == 4 || count == 9) {
                Ingredient first = ingredients.get(0);
                boolean passes = true;
                for(int i = 1; i < count; i++) {
                    if(first.getValidItemStacksPacked() != ingredients.get(i).getValidItemStacksPacked()) {
                        passes = false;
                        break;
                    }
                }
                if(count == 4 && recipe.canFit(2, 2)) {
                    if(passes) {
                        recipesSmall.add(new CompressedRecipe(first, 4, recipe.getRecipeOutput().copy()));
                    }
                } else if(count == 9 && recipe.canFit(3, 3)) {
                    recipes.add(new CompressedRecipe(first, 9, recipe.getRecipeOutput().copy()));
                }
            }
        }
    }

    @Nullable
    public static CompressedRecipe getRecipe(ItemStack itemStack) {
        if(itemStack.getTagCompound() != null) {
            return null;
        }
        RegistryKey key = new RegistryKey(itemStack);
        CompressedRecipe foundRecipe = cachedResults.get(key);
        if(foundRecipe != null) {
            return foundRecipe;
        }
        for(CompressedRecipe recipe : recipes) {
            if(recipe.getIngredient().apply(itemStack)) {
                cachedResults.put(key, recipe);
                return recipe;
            }
        }
        for(CompressedRecipe recipe : recipesSmall) {
            if(recipe.getIngredient().apply(itemStack)) {
                cachedResults.put(key, recipe);
                return recipe;
            }
        }
        cachedResults.put(key, null);
        return null;
    }

}
