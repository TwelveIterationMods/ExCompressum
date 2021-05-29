package net.blay09.mods.excompressum.compat.crafttweaker.builder;


import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.item.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.google.common.collect.Sets;
import net.blay09.mods.excompressum.api.sievemesh.MeshType;
import net.blay09.mods.excompressum.compat.jei.LootTableUtils;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Collections;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.HeavySieveRecipe")
public class ZenHeavySieveRecipe extends ZenBaseRecipe<ZenHeavySieveRecipe> {

    private final HeavySieveRecipe recipe;

    private ZenHeavySieveRecipe(ResourceLocation recipeId) {
        this.recipe = new HeavySieveRecipe(recipeId, Ingredient.EMPTY, LootTableProvider.EMPTY, false, MeshType.STRING, Collections.emptySet());
    }

    @ZenCodeType.Method
    public static ZenHeavySieveRecipe builder(ResourceLocation recipeId) {
        return new ZenHeavySieveRecipe(recipeId);
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe addDrop(IItemStack drop) {
        addLootPoolBuilder().addEntry(LootTableUtils.buildLootEntry(drop.getInternal(), 1f));
        recipe.setLootTable(getLootTableProvider());
        return this;
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe addDrop(IItemStack drop, float chance) {
        addLootPoolBuilder().addEntry(LootTableUtils.buildLootEntry(drop.getInternal(), chance));
        recipe.setLootTable(getLootTableProvider());
        return this;
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe setMinimumMesh(MeshType meshType) {
        recipe.setMinimumMesh(meshType);
        recipe.setMeshes(null);
        return this;
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe setMeshes(MeshType[] meshes) {
        recipe.setMeshes(Sets.newHashSet(meshes));
        recipe.setMinimumMesh(null);
        return this;
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe setInput(IIngredient input) {
        recipe.setInput(input.asVanillaIngredient());
        return this;
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe setWaterlogged() {
        recipe.setWaterlogged(true);
        return this;
    }

    @Override
    public void updateLootTable(LootTableProvider provider) {
        recipe.setLootTable(provider);
    }

    public HeavySieveRecipe build() {
        return recipe;
    }

}
