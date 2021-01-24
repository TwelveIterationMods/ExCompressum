package net.blay09.mods.excompressum.newregistry.woodencrucible;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.newregistry.ExCompressumRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class WoodenCrucibleRecipeSerializer extends ExCompressumRecipeSerializer<WoodenCrucibleRecipe> {

    public WoodenCrucibleRecipeSerializer() {
        setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "wooden_crucible"));
    }

    @Override
    public WoodenCrucibleRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
        ResourceLocation fluid = new ResourceLocation(JSONUtils.getString(json, "fluid"));
        int amount = JSONUtils.getInt(json, "amount");
        return new WoodenCrucibleRecipe(id, input, fluid, amount);
    }

    @Override
    public WoodenCrucibleRecipe read(ResourceLocation id, PacketBuffer buffer) {
        Ingredient input = Ingredient.read(buffer);
        ResourceLocation fluid = buffer.readResourceLocation();
        int amount = buffer.readInt();
        return new WoodenCrucibleRecipe(id, input, fluid, amount);
    }

    @Override
    public void write(PacketBuffer buffer, WoodenCrucibleRecipe recipe) {
        recipe.getInput().write(buffer);
        buffer.writeResourceLocation(recipe.getFluid());
        buffer.writeInt(recipe.getAmount());
    }

}
