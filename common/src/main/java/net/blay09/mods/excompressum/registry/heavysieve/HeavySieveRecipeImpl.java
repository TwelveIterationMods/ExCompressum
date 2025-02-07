package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import java.util.Set;

public class HeavySieveRecipeImpl extends ExCompressumRecipe implements HeavySieveRecipe {

    private Ingredient ingredient;
    private LootTable lootTable;
    private boolean waterlogged;
    private CommonMeshType minimumMesh;
    private Set<CommonMeshType> meshes;

    public HeavySieveRecipeImpl(ResourceLocation id, Ingredient ingredient, LootTable lootTable, boolean waterlogged, @Nullable CommonMeshType minimumMesh, @Nullable Set<CommonMeshType> meshes) {
        super(id, ModRecipeTypes.heavySieveRecipeType);
        this.ingredient = ingredient;
        this.lootTable = lootTable;
        this.waterlogged = waterlogged;
        this.minimumMesh = minimumMesh;
        this.meshes = meshes;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.heavySieveRecipeSerializer;
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
    public CommonMeshType getMinimumMesh() {
        return minimumMesh;
    }

    @Nullable
    public Set<CommonMeshType> getMeshes() {
        return meshes;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setLootTable(LootTable lootTable) {
        this.lootTable = lootTable;
    }

    public void setWaterlogged(boolean waterlogged) {
        this.waterlogged = waterlogged;
    }

    public void setMinimumMesh(@Nullable CommonMeshType minimumMesh) {
        this.minimumMesh = minimumMesh;
    }

    public void setMeshes(@Nullable Set<CommonMeshType> meshes) {
        this.meshes = meshes;
    }
}
