package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.api.sievemesh.MeshType;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Set;

public class HeavySieveRecipe extends ExCompressumRecipe {
    public static final IRecipeType<HeavySieveRecipe> TYPE = IRecipeType.register(ExCompressum.MOD_ID + ":heavy_sieve");

    private Ingredient input;
    private LootTableProvider lootTable;
    private boolean waterlogged;
    private MeshType minimumMesh;
    private Set<MeshType> meshes;

    public HeavySieveRecipe(ResourceLocation id, Ingredient input, LootTableProvider lootTable, boolean waterlogged, @Nullable MeshType minimumMesh, @Nullable Set<MeshType> meshes) {
        super(id, TYPE);
        this.input = input;
        this.lootTable = lootTable;
        this.waterlogged = waterlogged;
        this.minimumMesh = minimumMesh;
        this.meshes = meshes;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
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
    public MeshType getMinimumMesh() {
        return minimumMesh;
    }

    @Nullable
    public Set<MeshType> getMeshes() {
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

    public void setMinimumMesh(@Nullable MeshType minimumMesh) {
        this.minimumMesh = minimumMesh;
    }

    public void setMeshes(@Nullable Set<MeshType> meshes) {
        this.meshes = meshes;
    }
}
