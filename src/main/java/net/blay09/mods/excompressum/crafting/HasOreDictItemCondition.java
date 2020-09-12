package net.blay09.mods.excompressum.crafting;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class HasOreDictItemCondition implements ICondition {

    private static final ResourceLocation NAME = new ResourceLocation(ExCompressum.MOD_ID, "has_ore_dict_item");
    private final String key;

    public HasOreDictItemCondition(String key) {
        this.key = key;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return false; // TODO !OreDictionary.getOres(key, false).isEmpty();
    }

    public static class Serializer implements IConditionSerializer<HasOreDictItemCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, HasOreDictItemCondition value) {
            json.addProperty("value", value.key);
        }

        @Override
        public HasOreDictItemCondition read(JsonObject json) {
            return new HasOreDictItemCondition(JSONUtils.getString(json, "value"));
        }

        @Override
        public ResourceLocation getID() {
            return HasOreDictItemCondition.NAME;
        }

    }

}
