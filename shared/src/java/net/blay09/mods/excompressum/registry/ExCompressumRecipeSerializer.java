package net.blay09.mods.excompressum.registry;

import com.google.gson.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class ExCompressumRecipeSerializer<T extends Recipe<?>> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {

    private static final Gson gson = Deserializers.createLootTableSerializer().create();

    @Override
    public final T fromJson(ResourceLocation id, JsonObject json) {
        if (CraftingHelper.processConditions(json, "conditions")) {
            return readFromJson(id, json);
        } else {
            //noinspection ConstantConditions
            return null;
        }
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
        String jsonString = buffer.readUtf();
        JsonObject jsonElement = gson.fromJson(jsonString, JsonObject.class);
        return new LootTableProvider(jsonElement);
    }

    public void writeLootTable(FriendlyByteBuf buffer, ResourceLocation recipeId, LootTableProvider lootTableProvider) {
        LootTables lootTableManager = ServerLifecycleHooks.getCurrentServer().getLootTables();
        LootTable lootTable = lootTableProvider.getLootTable(recipeId, lootTableManager);
        String jsonString = gson.toJson(lootTable);
        buffer.writeUtf(jsonString);
    }
}
