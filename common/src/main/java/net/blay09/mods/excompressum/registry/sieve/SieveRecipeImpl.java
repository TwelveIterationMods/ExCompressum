package net.blay09.mods.excompressum.registry.sieve;

import net.blay09.mods.excompressum.api.recipe.SieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class SieveRecipeImpl extends ExCompressumRecipe implements SieveRecipe {

    private final Ingredient ingredient;
    private final LootTable lootTable;
    private final boolean waterlogged;
    private final Set<CommonMeshType> meshes;

    public SieveRecipeImpl(Ingredient ingredient, LootTable lootTable, boolean waterlogged, Set<CommonMeshType> meshes) {
        this.ingredient = ingredient;
        this.lootTable = lootTable;
        this.waterlogged = waterlogged;
        this.meshes = meshes;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.heavySieve.serializer();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeTypes.heavySieve.type();
    }

    @Override
    public PlacementInfo placementInfo() {
        return PlacementInfo.create(ingredient);
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return ModRecipeTypes.heavySieve.bookCategory();
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    public boolean isWaterlogged() {
        return waterlogged;
    }

    @Nullable
    public Set<CommonMeshType> getMeshes() {
        return meshes;
    }
}
