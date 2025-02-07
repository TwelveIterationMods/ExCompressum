package net.blay09.mods.excompressum.registry.serialization;

import com.google.gson.*;
import net.minecraft.world.item.crafting.Ingredient;

import java.lang.reflect.Type;

public class IngredientSerializer implements JsonDeserializer<Ingredient>, JsonSerializer<Ingredient> {
    @Override
    public Ingredient deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return Ingredient.fromJson(json);
    }

    @Override
    public JsonElement serialize(Ingredient itemStack, Type type, JsonSerializationContext context) {
        throw new UnsupportedOperationException("Serialization of Ingredient to JSON is not implemented");
    }
}

