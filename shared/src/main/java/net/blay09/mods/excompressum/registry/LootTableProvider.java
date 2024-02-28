package net.blay09.mods.excompressum.registry;

import com.google.gson.*;
import net.blay09.mods.excompressum.api.ILootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.*;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public class LootTableProvider implements ILootTableProvider {

    private static final Gson GSON_INSTANCE = Deserializers.createLootTableSerializer().create();
    public static final LootTableProvider EMPTY = new LootTableProvider(LootTable.EMPTY);

    private ResourceLocation lootTableLocation;
    private JsonObject inlineLootTableJson;
    private LootTable inlineLootTable;

    public LootTableProvider(JsonObject inlineLootTableJson) {
        this.inlineLootTableJson = inlineLootTableJson;
    }

    public LootTableProvider(ResourceLocation lootTableLocation) {
        this.lootTableLocation = lootTableLocation;
    }

    public LootTableProvider(LootTable inlineLootTable) {
        this.inlineLootTable = inlineLootTable;
    }

    @Nullable
    public LootTable getLootTable(@Nullable ResourceLocation resourceLocation, LootDataResolver lootDataResolver) {
        if (inlineLootTableJson != null || inlineLootTable != null) {
            if (inlineLootTable == null) {
                // TODO inlineLootTable = ForgeHooks.loadLootTable(GSON_INSTANCE, resourceLocation, inlineLootTableJson, true);
            }
            return inlineLootTable;
        }

        return lootDataResolver.getLootTable(lootTableLocation);
    }

    @Nullable
    public LootTable getLootTable(ResourceLocation resourceLocation, LootContext context) {
        if (inlineLootTableJson != null || inlineLootTable != null) {
            if (inlineLootTable == null) {
                // TODO inlineLootTable = ForgeHooks.loadLootTable(GSON_INSTANCE, resourceLocation, inlineLootTableJson, true);
            }
            return inlineLootTable;
        }

        return context.getResolver().getLootTable(lootTableLocation);
    }

    public static class Serializer implements JsonDeserializer<LootTableProvider>, JsonSerializer<LootTableProvider> {

        @Override
        public LootTableProvider deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive()) {
                return new LootTableProvider(new ResourceLocation(json.getAsString()));
            } else {
                return new LootTableProvider(json.getAsJsonObject());
            }
        }

        @Override
        public JsonElement serialize(LootTableProvider provider, Type type, JsonSerializationContext context) {
            throw new UnsupportedOperationException("Serialization of LootTableProvider to JSON is not implemented");
        }
    }
}
