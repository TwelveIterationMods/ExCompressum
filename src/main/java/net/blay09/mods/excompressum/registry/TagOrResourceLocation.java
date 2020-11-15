package net.blay09.mods.excompressum.registry;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Type;

public class TagOrResourceLocation {

    private final ResourceLocation resourceLocation;
    private final boolean isTag;

    private TagOrResourceLocation(ResourceLocation resourceLocation, boolean isTag) {
        this.resourceLocation = resourceLocation;
        this.isTag = isTag;
    }

    public ResourceLocation getResourceLocation() {
        return resourceLocation;
    }

    public boolean isTag() {
        return isTag;
    }

    public static class Serializer implements JsonDeserializer<TagOrResourceLocation>, JsonSerializer<TagOrResourceLocation> {

        @Override
        public TagOrResourceLocation deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonPrimitive()) {
                String value = json.getAsString();
                boolean isTag = false;
                if(value.startsWith("#")) {
                    value = value.substring(1);
                    isTag = true;
                }
                return new TagOrResourceLocation(new ResourceLocation(value), isTag);
            } else {
                throw new IllegalArgumentException("expected a string");
            }
        }

        @Override
        public JsonElement serialize(TagOrResourceLocation provider, Type type, JsonSerializationContext context) {
            throw new UnsupportedOperationException("Serialization of TagOrResourceLocation to JSON is not implemented");
        }
    }
}
