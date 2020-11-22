package net.blay09.mods.excompressum.registry;

import com.google.gson.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.lang.reflect.Type;

public class IngredientSerializer implements JsonDeserializer<Ingredient>, JsonSerializer<Ingredient> {
    @Override
    public Ingredient deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Ingredient.deserialize(json);
    }

    @Override
    public JsonElement serialize(Ingredient itemStack, Type type, JsonSerializationContext context) {
        throw new UnsupportedOperationException("Serialization of Ingredient to JSON is not implemented");
    }
}

