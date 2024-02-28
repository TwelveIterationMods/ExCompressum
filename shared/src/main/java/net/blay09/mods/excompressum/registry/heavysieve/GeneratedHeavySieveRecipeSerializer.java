package net.blay09.mods.excompressum.registry.heavysieve;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class GeneratedHeavySieveRecipeSerializer extends ExCompressumRecipeSerializer<GeneratedHeavySieveRecipe> {

    @Override
    public GeneratedHeavySieveRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        ResourceLocation source = new ResourceLocation(GsonHelper.getAsString(json, "source"));
        Integer rolls = null;
        if (GsonHelper.isValidNode(json, "rolls")) {
            rolls = GsonHelper.getAsInt(json, "rolls");
        }
        return new GeneratedHeavySieveRecipe(id, input, source, rolls);
    }

    @Override
    public GeneratedHeavySieveRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ResourceLocation source = buffer.readResourceLocation();
        int rolls = buffer.readByte();
        return new GeneratedHeavySieveRecipe(id, input, source, rolls != -1 ? rolls : null);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, GeneratedHeavySieveRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeResourceLocation(recipe.getSource());
        buffer.writeByte(recipe.getRolls() != null ? recipe.getRolls() : -1);
    }

}
