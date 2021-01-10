package net.blay09.mods.excompressum.api;

import com.google.gson.*;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootSerializers;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public class LootTableProvider { // TODO interface

    private static final Gson GSON_INSTANCE = LootSerializers.func_237388_c_().create();

    private ResourceLocation lootTableLocation;
    private JsonObject inlineLootTableJson;
    private LootTable inlineLootTable;

    private LootTableProvider(JsonObject inlineLootTableJson) {
        this.inlineLootTableJson = inlineLootTableJson;
    }

    public LootTableProvider(ResourceLocation lootTableLocation) {
        this.lootTableLocation = lootTableLocation;
    }

    public LootTableProvider(LootTable inlineLootTable) {
        this.inlineLootTable = inlineLootTable;
    }

    @Nullable
    public LootTable getLootTable(String name, LootTableManager lootTableManager) {
        if (inlineLootTableJson != null || inlineLootTable != null) {
            if (inlineLootTable == null) {
                inlineLootTable = ForgeHooks.loadLootTable(GSON_INSTANCE, new ResourceLocation(ExCompressumAPI.MOD_ID, name + "_embed"), inlineLootTableJson, true, lootTableManager);
            }
            return inlineLootTable;
        }

        return lootTableManager.getLootTableFromLocation(lootTableLocation);
    }

    @Nullable
    public LootTable getLootTable(String name, LootContext context) {
        if (inlineLootTableJson != null || inlineLootTable != null) {
            if (inlineLootTable == null) {
                inlineLootTable = ForgeHooks.loadLootTable(GSON_INSTANCE, new ResourceLocation(ExCompressumAPI.MOD_ID, name + "_embed"), inlineLootTableJson, true, context.getWorld().getServer().getLootTableManager());
            }
            return inlineLootTable;
        }

        return context.getLootTable(lootTableLocation);
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
