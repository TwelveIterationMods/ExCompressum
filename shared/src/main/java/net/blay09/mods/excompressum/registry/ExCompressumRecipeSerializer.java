package net.blay09.mods.excompressum.registry;

import com.google.gson.*;
import net.blay09.mods.balm.api.Balm;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.Deserializers;

public abstract class ExCompressumRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {

    private static final Gson gson = Deserializers.createLootTableSerializer().create();

    @Override
    public final T fromJson(ResourceLocation id, JsonObject json) {
        return readFromJson(id, json);
    }

    public abstract T readFromJson(ResourceLocation id, JsonObject json);

    public LootTableProvider readLootTableProvider(JsonObject json, String field) throws JsonParseException {
        if (GsonHelper.isStringValue(json, field)) {
            return new LootTableProvider(new ResourceLocation(json.getAsString()));
        } else {
            return new LootTableProvider(GsonHelper.getAsJsonObject(json, field));
        }
    }

    public LootTableProvider readLootTableProvider(FriendlyByteBuf buffer) {
        final var jsonString = buffer.readUtf();
        final var jsonElement = gson.fromJson(jsonString, JsonObject.class);
        return new LootTableProvider(jsonElement);
    }

    public void writeLootTable(FriendlyByteBuf buffer, ResourceLocation recipeId, LootTableProvider lootTableProvider) {
        final var lootTableManager = Balm.getHooks().getServer().getLootData();
        final var lootTable = lootTableProvider.getLootTable(recipeId, lootTableManager);
        final var jsonString = gson.toJson(lootTable);
        buffer.writeUtf(jsonString);
    }
}
