package net.blay09.mods.excompressum.compat.crafttweaker.builder;


import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import net.blay09.mods.excompressum.compat.crafttweaker.GeneratedHeavySieveRecipeManager;
import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Collections;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.GeneratedHeavySieveRecipe")
public class ZenGeneratedHeavySieveRecipe {

    private final GeneratedHeavySieveRecipe recipe;

    private ZenGeneratedHeavySieveRecipe(ResourceLocation recipeId) {
        this.recipe = new GeneratedHeavySieveRecipe(recipeId, Ingredient.EMPTY, new ResourceLocation("air"), null);
    }

    @ZenCodeType.Method
    public static ZenGeneratedHeavySieveRecipe builder(ResourceLocation recipeId) {
        return new ZenGeneratedHeavySieveRecipe(recipeId);
    }

    @ZenCodeType.Method
    public ZenGeneratedHeavySieveRecipe setSource(IItemStack source) {
        recipe.setSource(source.getRegistryName());
        return this;
    }

    @ZenCodeType.Method
    public ZenGeneratedHeavySieveRecipe setSource(IItemStack source, int rolls) {
        recipe.setSource(source.getRegistryName());
        recipe.setRolls(rolls);
        return this;
    }

    @ZenCodeType.Method
    public ZenGeneratedHeavySieveRecipe setRolls(int rolls) {
        recipe.setRolls(rolls);
        return this;
    }

    @ZenCodeType.Method
    public ZenGeneratedHeavySieveRecipe setInput(IIngredient input) {
        recipe.setInput(input.asVanillaIngredient());
        return this;
    }

    public GeneratedHeavySieveRecipe build() {
        return recipe;
    }

}
