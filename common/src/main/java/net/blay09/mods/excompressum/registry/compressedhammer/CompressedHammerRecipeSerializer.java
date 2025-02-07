package net.blay09.mods.excompressum.registry.compressedhammer;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

public class CompressedHammerRecipeSerializer extends ExCompressumRecipeSerializer<CompressedHammerRecipeImpl> {

    @Override
    public CompressedHammerRecipeImpl readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        LootTable lootTable = readLootTable(json, "lootTable", id);
        return new CompressedHammerRecipeImpl(id, input, lootTable);
    }

    @Override
    public CompressedHammerRecipeImpl fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        LootTable lootTable = readLootTable(buffer, id);
        return new CompressedHammerRecipeImpl(id, input, lootTable);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CompressedHammerRecipeImpl recipe) {
        recipe.getIngredient().toNetwork(buffer);
        writeLootTable(buffer, recipe.getLootTable());
    }

}
