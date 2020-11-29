package net.blay09.mods.excompressum.crafting;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class EvolvedOrechidEnabledCondition implements ICondition {

    private static final ResourceLocation NAME = new ResourceLocation(ExCompressum.MOD_ID, "evolved_orechid_enabled");

    @Override
    public ResourceLocation getID() {
        return NAME;
    }

    @Override
    public boolean test() {
        return ExCompressumConfig.COMMON.enableEvolvedOrechid.get();
    }

    public static class Serializer implements IConditionSerializer<EvolvedOrechidEnabledCondition> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public void write(JsonObject json, EvolvedOrechidEnabledCondition value) {
        }

        @Override
        public EvolvedOrechidEnabledCondition read(JsonObject json) {
            return new EvolvedOrechidEnabledCondition();
        }

        @Override
        public ResourceLocation getID() {
            return EvolvedOrechidEnabledCondition.NAME;
        }

    }

}
