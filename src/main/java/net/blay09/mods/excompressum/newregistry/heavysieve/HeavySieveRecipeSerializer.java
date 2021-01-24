package net.blay09.mods.excompressum.newregistry.heavysieve;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.LootTableProvider;
import net.blay09.mods.excompressum.api.sievemesh.MeshType;
import net.blay09.mods.excompressum.newregistry.ExCompressumRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class HeavySieveRecipeSerializer extends ExCompressumRecipeSerializer<HeavySieveRecipe> {

    public HeavySieveRecipeSerializer() {
        setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "heavy_sieve"));
    }

    @Override
    public HeavySieveRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.deserialize(json.get("input"));
        LootTableProvider lootTable = readLootTableProvider(json, "lootTable");
        boolean waterlogged = JSONUtils.getBoolean(json, "waterlogged", false);
        String minimumMeshName = JSONUtils.getString(json, "minimumMesh", "");
        MeshType minimumMesh = !minimumMeshName.isEmpty() ? MeshType.valueOf(minimumMeshName) : null; // TODO error handling
        JsonArray meshNames = JSONUtils.getJsonArray(json, "meshes", null);
        Set<MeshType> meshes = null;
        if (meshNames != null) {
            meshes = new HashSet<>();
            for (JsonElement meshName : meshNames) {
                meshes.add(MeshType.valueOf(meshName.getAsString())); // TODO error handling
            }
        }

        // TODO replace? if false, don't override existing, else override existing (aka same-id)

        return new HeavySieveRecipe(id, input, lootTable, waterlogged, minimumMesh, meshes);
    }

    @Override
    public HeavySieveRecipe read(ResourceLocation id, PacketBuffer buffer) {
        Ingredient input = Ingredient.read(buffer);
        LootTableProvider lootTable = readLootTableProvider(buffer);
        boolean waterlogged = buffer.readBoolean();
        int meshCount = buffer.readByte();
        final MeshType[] meshTypes = MeshType.values();
        MeshType minimumMesh = null;
        Set<MeshType> meshes = null;
        if (meshCount == -1) {
            minimumMesh = meshTypes[buffer.readByte()];
        } else {
            meshes = new HashSet<>();
            for (int i = 0; i < meshCount; i++) {
                meshes.add(meshTypes[buffer.readByte()]);
            }
        }

        return new HeavySieveRecipe(id, input, lootTable, waterlogged, minimumMesh, meshes);
    }

    @Override
    public void write(PacketBuffer buffer, HeavySieveRecipe recipe) {
        recipe.getInput().write(buffer);
        writeLootTable(buffer, recipe.getId(), recipe.getLootTable());
        buffer.writeBoolean(recipe.isWaterlogged());
        if (recipe.getMinimumMesh() != null) {
            buffer.writeByte(-1);
            buffer.writeByte(recipe.getMinimumMesh().ordinal());
        } else if(recipe.getMeshes() != null) {
            buffer.writeByte(recipe.getMeshes().size());
            for (MeshType mesh : recipe.getMeshes()) {
                buffer.writeByte(mesh.ordinal());
            }
        } else {
            buffer.writeByte(0);
        }
    }

}