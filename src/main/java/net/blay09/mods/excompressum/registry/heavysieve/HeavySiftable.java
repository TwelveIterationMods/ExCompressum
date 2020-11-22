package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.RegistryEntry;
import net.blay09.mods.excompressum.api.sievemesh.MeshType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Set;

public class HeavySiftable extends RegistryEntry {

    private ResourceLocation id;
    private Ingredient source;
    private LootTableProvider lootTable;
    private MeshType minimumMesh;
    private Set<MeshType> meshes;
    private boolean waterlogged;

    public Ingredient getSource() {
        return source;
    }

    public void setSource(Ingredient source) {
        this.source = source;
    }

    @Nullable
    public LootTableProvider getLootTable() {
        return lootTable;
    }

    public void setLootTable(@Nullable LootTableProvider lootTable) {
        this.lootTable = lootTable;
    }

    @Nullable
    public LootTable getLootTable(LootContext context) {
        return lootTable != null ? lootTable.getLootTable(id.getPath(), context) : null;
    }

    @Nullable
    public MeshType getMinimumMesh() {
        return minimumMesh;
    }

    public void setMinimumMesh(@Nullable MeshType minimumMesh) {
        this.minimumMesh = minimumMesh;
    }

    @Nullable
    public Set<MeshType> getMeshes() {
        return meshes;
    }

    public void setMeshes(@Nullable Set<MeshType> meshes) {
        this.meshes = meshes;
    }

    public boolean isWaterlogged() {
        return waterlogged;
    }

    public void setWaterlogged(boolean waterlogged) {
        this.waterlogged = waterlogged;
    }

    public void setId(String id) {
        this.id = new ResourceLocation(ExCompressum.MOD_ID, id);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
