package net.blay09.mods.excompressum.registry;

import com.google.gson.*;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootTable;

public abstract class ExCompressumRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {

    private static final Gson gson = Deserializers.createLootTableSerializer().create();

    @Override
    public final T fromJson(ResourceLocation id, JsonObject json) {
        return readFromJson(id, json);
    }

    public abstract T readFromJson(ResourceLocation id, JsonObject json);

    public LootTable readLootTable(JsonObject json, String field, ResourceLocation lootTableId) throws JsonParseException {
        return ExCompressum.lootTableLoader.apply(gson, lootTableId, GsonHelper.getAsJsonObject(json, field));
    }

    public LootTable readLootTable(FriendlyByteBuf buffer, ResourceLocation lootTableId) {
        final var jsonString = buffer.readUtf();
        final var jsonElement = gson.fromJson(jsonString, JsonObject.class);
        return ExCompressum.lootTableLoader.apply(gson, lootTableId, jsonElement);
    }

    public void writeLootTable(FriendlyByteBuf buffer, LootTable lootTable) {
        final var jsonString = gson.toJson(lootTable);
        buffer.writeUtf(jsonString);
    }
}
