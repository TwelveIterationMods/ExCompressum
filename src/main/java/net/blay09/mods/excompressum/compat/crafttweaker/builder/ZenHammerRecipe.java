package net.blay09.mods.excompressum.compat.crafttweaker.builder;


import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.google.common.collect.Sets;
import net.blay09.mods.excompressum.api.sievemesh.MeshType;
import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Collections;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.HammerRecipe")
public class ZenHammerRecipe extends ZenBaseRecipe<ZenHammerRecipe> {

    private final HammerRecipe recipe;

    private ZenHammerRecipe(ResourceLocation recipeId) {
        this.recipe = new HammerRecipe(recipeId, Ingredient.EMPTY, LootTableProvider.EMPTY);
    }

    @ZenCodeType.Method
    public static ZenHammerRecipe builder(ResourceLocation recipeId) {
        return new ZenHammerRecipe(recipeId);
    }

    @ZenCodeType.Method
    public ZenHammerRecipe addDrop(IItemStack drop) {
        getLootPoolBuilder().addEntry(LootTableUtils.buildLootEntry(drop.getInternal(), 1f));
        recipe.setLootTable(getLootTableProvider());
        return this;
    }

    @ZenCodeType.Method
    public ZenHammerRecipe addDrop(IItemStack drop, float chance) {
        getLootPoolBuilder().addEntry(LootTableUtils.buildLootEntry(drop.getInternal(), chance));
        recipe.setLootTable(getLootTableProvider());
        return this;
    }

    @ZenCodeType.Method
    public ZenHammerRecipe setInput(IIngredient input) {
        recipe.setInput(input.asVanillaIngredient());
        return this;
    }

    @Override
    public void updateLootTable(LootTableProvider provider) {
        recipe.setLootTable(provider);
    }

    public HammerRecipe build() {
        return recipe;
    }

}
