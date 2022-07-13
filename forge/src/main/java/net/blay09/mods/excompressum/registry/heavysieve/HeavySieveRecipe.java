package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.Set;

public class HeavySieveRecipe extends ExCompressumRecipe {

    private Ingredient input;
    private LootTableProvider lootTable;
    private boolean waterlogged;
    private CommonMeshType minimumMesh;
    private Set<CommonMeshType> meshes;

    public HeavySieveRecipe(ResourceLocation id, Ingredient input, LootTableProvider lootTable, boolean waterlogged, @Nullable CommonMeshType minimumMesh, @Nullable Set<CommonMeshType> meshes) {
        super(id, ModRecipeTypes.HEAVY_SIEVE);
        this.input = input;
        this.lootTable = lootTable;
        this.waterlogged = waterlogged;
        this.minimumMesh = minimumMesh;
        this.meshes = meshes;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.heavySieveRecipe;
    }

    public Ingredient getInput() {
        return input;
    }

    public LootTableProvider getLootTable() {
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

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public void setLootTable(LootTableProvider lootTable) {
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
