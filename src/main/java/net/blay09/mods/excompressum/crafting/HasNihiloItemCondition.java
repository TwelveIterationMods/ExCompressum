package net.blay09.mods.excompressum.crafting;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class HasNihiloItemCondition implements ICondition {

    private static final ResourceLocation NAME = new ResourceLocation(ExCompressum.MOD_ID, "has_nihilo_item");
    private final String key;

    public HasNihiloItemCondition(String key) {
        this.key = key;
    }

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        ExNihiloProvider.NihiloItems nihiloItem = ExNihiloProvider.NihiloItems.valueOf(key);
        return !ExNihilo.getInstance().getNihiloItem(nihiloItem).isEmpty();
    }

    public static class Serializer implements IConditionSerializer<HasNihiloItemCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, HasNihiloItemCondition value) {
            json.addProperty("value", value.key);
        }

        @Override
        public HasNihiloItemCondition read(JsonObject json) {
            return new HasNihiloItemCondition(JSONUtils.getString(json, "value"));
        }

        @Override
        public ResourceLocation getID() {
            return HasNihiloItemCondition.NAME;
        }

    }

}
