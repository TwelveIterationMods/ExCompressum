package net.blay09.mods.excompressum.registry.serialization;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    @Override
    public ItemStack deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            Item item = GsonHelper.getAsItem(json.getAsJsonObject(), "item");
            return new ItemStack(item);
        } else {
            JsonObject jsonObject = json.getAsJsonObject();
            Item item = GsonHelper.getAsItem(jsonObject, "item");
            int count = GsonHelper.getAsInt(jsonObject, "count", 1);
            ItemStack itemStack = new ItemStack(item, count);
            JsonObject nbtJson = GsonHelper.getAsJsonObject(jsonObject, "nbt", new JsonObject());
            if (nbtJson.size() > 0) {
                try {
                    CompoundTag tagFromJson = TagParser.parseTag(nbtJson.toString());
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

