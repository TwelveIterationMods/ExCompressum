package net.blay09.mods.excompressum.registry;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            Item item = JSONUtils.getItem(json, "item");
            return new ItemStack(item);
        } else {
            JsonObject jsonObject = json.getAsJsonObject();
            Item item = JSONUtils.getItem(jsonObject, "item");
            int count = JSONUtils.getInt(jsonObject, "count", 1);
            ItemStack itemStack = new ItemStack(item, count);
            JsonObject nbtJson = JSONUtils.getJsonObject(jsonObject, "nbt", new JsonObject());
            if (nbtJson.size() > 0) {
                try {
                    CompoundNBT tagFromJson = JsonToNBT.getTagFromJson(nbtJson.toString());
                    itemStack.setTag(tagFromJson);
                } catch (CommandSyntaxException e) {
                    ExCompressum.logger.error("Failed to parse nbt data for itemstack {}x {}: ", item, count, e);
                }
            }
            return itemStack;
        }
    }

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        throw new UnsupportedOperationException("Serialization of ItemStack to JSON is not implemented");
    }
}

