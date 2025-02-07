package net.blay09.mods.excompressum.registry.heavysieve;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.blay09.mods.excompressum.api.sievemesh.CommonMeshType;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class HeavySieveRecipeSerializer extends ExCompressumRecipeSerializer<HeavySieveRecipeImpl> {

    @Override
    public HeavySieveRecipeImpl readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        LootTable lootTable = readLootTable(json, "lootTable", id);
        boolean waterlogged = GsonHelper.getAsBoolean(json, "waterlogged", false);
        String minimumMeshName = GsonHelper.getAsString(json, "minimumMesh", "");
        CommonMeshType minimumMesh = parseMeshType(minimumMeshName);
        JsonArray meshNames = GsonHelper.getAsJsonArray(json, "meshes", null);
        Set<CommonMeshType> meshes = null;
        if (meshNames != null) {
            meshes = new HashSet<>();
            for (JsonElement meshName : meshNames) {
                meshes.add(parseMeshType(meshName.getAsString()));
            }
        }

        return new HeavySieveRecipeImpl(id, input, lootTable, waterlogged, minimumMesh, meshes);
    }

    @Nullable
    private CommonMeshType parseMeshType(String minimumMeshName) {
        try {
            return !minimumMeshName.isEmpty() ? CommonMeshType.valueOf(minimumMeshName) : null;
        } catch (IllegalArgumentException e) {
            String validMeshes = Arrays.stream(CommonMeshType.values()).map(Enum::name).collect(Collectors.joining(", "));
            throw new JsonSyntaxException("Expected minimumMesh to be onf of [" + validMeshes + "] but got '" + minimumMeshName + "'");
        }
    }

    @Override
    public HeavySieveRecipeImpl fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        LootTable lootTable = readLootTable(buffer, id);
        boolean waterlogged = buffer.readBoolean();
        int meshCount = buffer.readByte();
        final CommonMeshType[] meshTypes = CommonMeshType.values();
        CommonMeshType minimumMesh = null;
        Set<CommonMeshType> meshes = null;
        if (meshCount == -1) {
            minimumMesh = meshTypes[buffer.readByte()];
        } else {
            meshes = new HashSet<>();
            for (int i = 0; i < meshCount; i++) {
                meshes.add(meshTypes[buffer.readByte()]);
            }
        }

        return new HeavySieveRecipeImpl(id, input, lootTable, waterlogged, minimumMesh, meshes);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, HeavySieveRecipeImpl recipe) {
        recipe.getIngredient().toNetwork(buffer);
        writeLootTable(buffer, recipe.getLootTable());
        buffer.writeBoolean(recipe.isWaterlogged());
        if (recipe.getMinimumMesh() != null) {
            buffer.writeByte(-1);
            buffer.writeByte(recipe.getMinimumMesh().ordinal());
        } else if (recipe.getMeshes() != null) {
            buffer.writeByte(recipe.getMeshes().size());
            for (CommonMeshType mesh : recipe.getMeshes()) {
                buffer.writeByte(mesh.ordinal());
            }
        } else {
            buffer.writeByte(0);
        }
    }

}