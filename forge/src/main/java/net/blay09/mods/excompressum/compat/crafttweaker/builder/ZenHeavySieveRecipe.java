package net.blay09.mods.excompressum.compat.crafttweaker.builder;


import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import com.blamejared.crafttweaker.api.item.IItemStack;
import com.google.common.collect.Sets;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.loot.LootTableUtils;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.openzen.zencode.java.ZenCodeType;

import java.util.Collections;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.HeavySieveRecipe")
public class ZenHeavySieveRecipe extends ZenBaseRecipe<ZenHeavySieveRecipe> {

    private final HeavySieveRecipe recipe;

    private ZenHeavySieveRecipe(ResourceLocation recipeId) {
        this.recipe = new HeavySieveRecipe(recipeId, Ingredient.EMPTY, LootTableProvider.EMPTY, false, CommonMeshType.STRING, Collections.emptySet());
    }

    @ZenCodeType.Method
    public static ZenHeavySieveRecipe builder(ResourceLocation recipeId) {
        return new ZenHeavySieveRecipe(recipeId);
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe addDrop(IItemStack drop) {
        addLootPoolBuilder().add(LootTableUtils.buildLootEntry(drop.getInternal(), 1f));
        recipe.setLootTable(getLootTableProvider());
        return this;
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe addDrop(IItemStack drop, float chance) {
        addLootPoolBuilder().add(LootTableUtils.buildLootEntry(drop.getInternal(), chance));
        recipe.setLootTable(getLootTableProvider());
        return this;
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe setMinimumMesh(CommonMeshType meshType) {
        recipe.setMinimumMesh(meshType);
        recipe.setMeshes(null);
        return this;
    }

    @ZenCodeType.Method
    public ZenHeavySieveRecipe setMeshes(CommonMeshType[] meshes) {
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
