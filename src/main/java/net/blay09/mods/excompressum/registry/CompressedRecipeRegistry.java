package net.blay09.mods.excompressum.registry;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.registry.data.CompressedRecipe;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Collections;
import java.util.List;

public class CompressedRecipeRegistry {

    private static final List<CompressedRecipe> recipesSmall = Lists.newArrayList();
    private static final List<CompressedRecipe> recipes = Lists.newArrayList();
    private static final InventoryCompressedMatcher matcherSmall = new InventoryCompressedMatcher(2, 2);
    private static final InventoryCompressedMatcher matcher = new InventoryCompressedMatcher(3, 3);

    public static void reload() {
        recipes.clear();
        for(Object obj : CraftingManager.getInstance().getRecipeList()) {
            IRecipe recipe = (IRecipe) obj;
            if(obj instanceof ShapedRecipes) {
                addCompressedRecipe(recipe, getRecipeSource((ShapedRecipes) obj));
            } else if(obj instanceof ShapelessRecipes) {
                addCompressedRecipe(recipe, getRecipeSource((ShapelessRecipes) obj));
            } else if(obj instanceof ShapedOreRecipe) {
                for(ItemStack itemStack : getRecipeSources((ShapedOreRecipe) obj)) {
                    addCompressedRecipe(recipe, itemStack);
                }
            } else if(obj instanceof ShapelessOreRecipe) {
                for(ItemStack itemStack : getRecipeSources((ShapelessOreRecipe) obj)) {
                    addCompressedRecipe(recipe, itemStack);
                }
            }
        }
    }

    private static void addCompressedRecipe(IRecipe recipe, ItemStack sourceStack) {
        if(sourceStack != null && sourceStack.getItem() != null) { // .getItem() != null is needed because some mod is registering a broken recipe
            sourceStack = sourceStack.copy();
            if(recipe.getRecipeSize() == 4) {
                matcherSmall.fill(sourceStack);
                if(recipe.matches(matcherSmall, null)) {
                    sourceStack.stackSize = 4;
                    recipesSmall.add(new CompressedRecipe(sourceStack, recipe.getCraftingResult(matcherSmall).copy()));
                }
            } else if(recipe.getRecipeSize() == 9) {
                matcher.fill(sourceStack);
                if(recipe.matches(matcher, null)) {
                    sourceStack.stackSize = 9;
                    recipes.add(new CompressedRecipe(sourceStack, recipe.getCraftingResult(matcher).copy()));
                }
            }
        }
    }

    private static ItemStack getRecipeSource(ShapedRecipes recipe) {
        for(ItemStack itemStack : recipe.recipeItems) {
            if(itemStack != null) {
                return itemStack;
            }
        }
        return null;
    }

    private static ItemStack getRecipeSource(ShapelessRecipes recipe) {
        for(Object obj : recipe.recipeItems) {
            if(obj != null) {
                return (ItemStack) obj;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static List<ItemStack> getRecipeSources(ShapedOreRecipe recipe) {
        for(Object obj : recipe.getInput()) {
            if(obj != null) {
                if(obj instanceof List) {
                    return (List<ItemStack>) obj;
                } else if(obj instanceof ItemStack) {
                    return Collections.singletonList((ItemStack) obj);
                } else if(obj instanceof Block) {
                    return Collections.singletonList(new ItemStack((Block) obj));
                } else if(obj instanceof Item) {
                    return Collections.singletonList(new ItemStack((Item) obj));
                }
            }
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    private static List<ItemStack> getRecipeSources(ShapelessOreRecipe recipe) {
        for(Object obj : recipe.getInput()) {
            if(obj != null) {
                if(obj instanceof List) {
                    return (List<ItemStack>) obj;
                } else if(obj instanceof ItemStack) {
                    return Collections.singletonList((ItemStack) obj);
                } else if(obj instanceof Block) {
                    return Collections.singletonList(new ItemStack((Block) obj));
                } else if(obj instanceof Item) {
                    return Collections.singletonList(new ItemStack((Item) obj));
                }
            }
        }
        return Collections.emptyList();
    }

    public static CompressedRecipe getRecipe(ItemStack itemStack) {
        if(itemStack == null) {
            return null;
        }
        for(CompressedRecipe recipe : recipes) {
            if(itemStack.isItemEqual(recipe.getSourceStack()) && ItemStack.areItemStackTagsEqual(itemStack, recipe.getSourceStack())) {
                return recipe;
            }
        }
        for(CompressedRecipe recipe : recipesSmall) {
            if(itemStack.isItemEqual(recipe.getSourceStack()) && ItemStack.areItemStackTagsEqual(itemStack, recipe.getSourceStack())) {
                return recipe;
            }
        }
        return null;
    }

}
