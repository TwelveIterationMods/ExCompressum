package net.blay09.mods.excompressum.registry.woodencrucible;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class WoodenCrucibleRecipeSerializer extends ExCompressumRecipeSerializer<WoodenCrucibleRecipe> {

    @Override
    public WoodenCrucibleRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        ResourceLocation fluid = new ResourceLocation(GsonHelper.getAsString(json, "fluid"));
        int amount = GsonHelper.getAsInt(json, "amount");
        return new WoodenCrucibleRecipe(id, input, fluid, amount);
    }

    @Override
    public WoodenCrucibleRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ResourceLocation fluid = buffer.readResourceLocation();
        int amount = buffer.readInt();
        return new WoodenCrucibleRecipe(id, input, fluid, amount);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, WoodenCrucibleRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        buffer.writeResourceLocation(recipe.getFluidId());
        buffer.writeInt(recipe.getAmount());
    }

}
