package net.blay09.mods.excompressum.registry.heavysieve.newregistry;

import com.google.gson.*;
import net.blay09.mods.excompressum.api.LootTableProvider;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ExCompressumRecipeSerializer<T extends IRecipe<?>> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LootTable.class, new LootTable.Serializer())
            .create();

    @Override
    public final T read(ResourceLocation id, JsonObject json) {
        if (CraftingHelper.processConditions(json, "conditions")) {
            return readFromJson(id, json);
        } else {
            //noinspection ConstantConditions
            return null;
        }
    }

    public abstract T readFromJson(ResourceLocation id, JsonObject json);

    public LootTableProvider readLootTableProvider(JsonObject json, String field) throws JsonParseException {
        if (JSONUtils.isString(json, field)) {
            return new LootTableProvider(new ResourceLocation(json.getAsString()));
        } else {
            return new LootTableProvider(JSONUtils.getJsonObject(json, field));
        }
    }

    public LootTableProvider readLootTableProvider(PacketBuffer buffer) {
        String jsonString = buffer.readString();
        LootTable lootTable = gson.fromJson(jsonString, LootTable.class);
        return new LootTableProvider(lootTable);
    }

    public void writeLootTable(PacketBuffer buffer, ResourceLocation recipeId, LootTableProvider lootTableProvider) {
        LootTableManager lootTableManager = ServerLifecycleHooks.getCurrentServer().getLootTableManager();
        LootTable lootTable = lootTableProvider.getLootTable(recipeId.toString(), lootTableManager);
        String jsonString = gson.toJson(lootTable);
        buffer.writeString(jsonString);
    }
}
