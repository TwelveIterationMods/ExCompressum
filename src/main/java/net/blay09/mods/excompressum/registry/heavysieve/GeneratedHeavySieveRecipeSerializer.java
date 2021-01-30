package net.blay09.mods.excompressum.registry.heavysieve;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class GeneratedHeavySieveRecipeSerializer extends ExCompressumRecipeSerializer<GeneratedHeavySieveRecipe> {

    public GeneratedHeavySieveRecipeSerializer() {
        setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "heavy_sieve_generated"));
    }

    @Override
    public GeneratedHeavySieveRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
        ResourceLocation source = new ResourceLocation(JSONUtils.getString(json, "source"));
        Integer rolls = null;
        if (JSONUtils.hasField(json, "rolls")) {
            rolls = JSONUtils.getInt(json, "rolls");
        }
        return new GeneratedHeavySieveRecipe(id, input, source, rolls);
    }

    @Override
    public GeneratedHeavySieveRecipe read(ResourceLocation id, PacketBuffer buffer) {
        Ingredient input = Ingredient.read(buffer);
        ResourceLocation source = buffer.readResourceLocation();
        int rolls = buffer.readByte();
        return new GeneratedHeavySieveRecipe(id, input, source, rolls != -1 ? rolls : null);
    }

    @Override
    public void write(PacketBuffer buffer, GeneratedHeavySieveRecipe recipe) {
        recipe.getInput().write(buffer);
        buffer.writeResourceLocation(recipe.getSource());
        buffer.writeByte(recipe.getRolls() != null ? recipe.getRolls() : -1);
    }

}
