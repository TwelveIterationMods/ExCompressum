package net.blay09.mods.excompressum.registry.compressor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.blay09.mods.excompressum.registry.RegistryKey;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CompressedRecipeRegistry {

    private static final List<CompressedRecipe> recipesSmall = Lists.newArrayList();
    private static final List<CompressedRecipe> recipes = Lists.newArrayList();
    private static final InventoryCompressedMatcher matcherSmall = new InventoryCompressedMatcher(2, 2, false);
    private static final InventoryCompressedMatcher matcherSmallStupid = new InventoryCompressedMatcher(3, 3, true);
    private static final InventoryCompressedMatcher matcher = new InventoryCompressedMatcher(3, 3, false);

    private static final Map<RegistryKey, CompressedRecipe> cachedResults = Maps.newHashMap();

    public static void reload() {
        cachedResults.clear();
        recipesSmall.clear();
        recipes.clear();
        for(IRecipe recipe : CraftingManager.REGISTRY) {
            // TODO fixme
//            if(obj instanceof ShapedRecipes) {
//                addCompressedRecipe(recipe, getRecipeSource((ShapedRecipes) obj));
//            } else if(obj instanceof ShapelessRecipes) {
//                addCompressedRecipe(recipe, getRecipeSource((ShapelessRecipes) obj));
//            } else if(obj instanceof ShapedOreRecipe) {
//                for(ItemStack itemStack : getRecipeSources((ShapedOreRecipe) obj)) {
//                    addCompressedRecipe(recipe, itemStack);
//                }
//            } else if(obj instanceof ShapelessOreRecipe) {
//                for(ItemStack itemStack : getRecipeSources((ShapelessOreRecipe) obj)) {
//                    addCompressedRecipe(recipe, itemStack);
//                }
//            }
        }
    }

    private static void addCompressedRecipe(IRecipe recipe, ItemStack sourceStack) {
        if(!sourceStack.isEmpty()) {
            sourceStack = sourceStack.copy();
            if(recipe.canFit(2, 2)) {
                matcherSmall.fill(sourceStack);
                if(recipe.matches(matcherSmall, null)) {
                    sourceStack.setCount(4);
                    ItemStack result = recipe.getCraftingResult(matcherSmall);
                    if(!result.isEmpty()) {
                        recipesSmall.add(new CompressedRecipe(sourceStack, result.copy()));
                    }
                }
            } else if(recipe.canFit(3, 3)) {
                matcher.fill(sourceStack);
                if(recipe.matches(matcher, null)) {
                    sourceStack.setCount(9);
                    ItemStack result = recipe.getCraftingResult(matcher);
                    if(!result.isEmpty()) {
                        recipes.add(new CompressedRecipe(sourceStack, result.copy()));
                    }
                } else { // Fallback for stupid mods that register 2x2 recipes in a 3x3 shaped grid
                    matcherSmallStupid.fill(sourceStack);
                    if(recipe.matches(matcherSmallStupid, null)) {
                        sourceStack.setCount(4);
                        ItemStack result = recipe.getCraftingResult(matcherSmallStupid);
                        if(!result.isEmpty()) {
                            recipesSmall.add(new CompressedRecipe(sourceStack, result.copy()));
                        }
                    }
                }
            }
        }
    }

//    private static ItemStack getRecipeSource(ShapedRecipes recipe) {
//        for(ItemStack itemStack : recipe.recipeItems) {
//            if(!itemStack.isEmpty()) {
//                return itemStack;
//            }
//        }
//        return ItemStack.EMPTY;
//    }
//
//    private static ItemStack getRecipeSource(ShapelessRecipes recipe) {
//        for(ItemStack obj : recipe.recipeItems) {
//            if(!obj.isEmpty()) {
//                return obj;
//            }
//        }
//        return ItemStack.EMPTY;
//    }
//
//    @SuppressWarnings("unchecked")
//    private static List<ItemStack> getRecipeSources(ShapedOreRecipe recipe) {
//        for(Object obj : recipe.getInput()) {
//            if(obj != null) {
//                if(obj instanceof List) {
//                    return (List<ItemStack>) obj;
//                } else if(obj instanceof ItemStack) {
//                    return Collections.singletonList((ItemStack) obj);
//                } else if(obj instanceof Block) {
//                    return Collections.singletonList(new ItemStack((Block) obj));
//                } else if(obj instanceof Item) {
//                    return Collections.singletonList(new ItemStack((Item) obj));
//                }
//            }
//        }
//        return Collections.emptyList();
//    }
//
//    @SuppressWarnings("unchecked")
//    private static List<ItemStack> getRecipeSources(ShapelessOreRecipe recipe) {
//        for(Object obj : recipe.getInput()) {
//            if(obj != null) {
//                if(obj instanceof List) {
//                    return (List<ItemStack>) obj;
//                } else if(obj instanceof ItemStack) {
//                    return Collections.singletonList((ItemStack) obj);
//                } else if(obj instanceof Block) {
//                    return Collections.singletonList(new ItemStack((Block) obj));
//                } else if(obj instanceof Item) {
//                    return Collections.singletonList(new ItemStack((Item) obj));
//                }
//            }
//        }
//        return Collections.emptyList();
//    }

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
            if(itemStack.getItem() == recipe.getSourceStack().getItem() && (recipe.getSourceStack().getItemDamage() == OreDictionary.WILDCARD_VALUE || recipe.getSourceStack().getItemDamage() == itemStack.getItemDamage())) {
                cachedResults.put(key, recipe);
                return recipe;
            }
        }
        for(CompressedRecipe recipe : recipesSmall) {
            if(itemStack.getItem() == recipe.getSourceStack().getItem() && (recipe.getSourceStack().getItemDamage() == OreDictionary.WILDCARD_VALUE || recipe.getSourceStack().getItemDamage() == itemStack.getItemDamage())) {
                cachedResults.put(key, recipe);
                return recipe;
            }
        }
        cachedResults.put(key, null);
        return null;
    }

}
