package net.blay09.mods.excompressum.registry;

import com.google.gson.*;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Objects;

public abstract class ExCompressumRecipeSerializer<T extends IRecipe<?>> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private static final Gson gson = LootSerializers.func_237388_c_().create();

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
        JsonObject jsonElement = gson.fromJson(jsonString, JsonObject.class);
        return new LootTableProvider(jsonElement);
    }

    public void writeLootTable(PacketBuffer buffer, ResourceLocation recipeId, LootTableProvider lootTableProvider) {
        LootTableManager lootTableManager = ServerLifecycleHooks.getCurrentServer().getLootTableManager();
        LootTable lootTable = lootTableProvider.getLootTable(recipeId, lootTableManager);
        String jsonString = gson.toJson(lootTable);
        buffer.writeString(jsonString);
    }
}
