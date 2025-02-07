package net.blay09.mods.excompressum.registry.chickenstick;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

public class ChickenStickRecipeSerializer extends ExCompressumRecipeSerializer<ChickenStickRecipe> {

    @Override
    public ChickenStickRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        LootTable lootTable = readLootTable(json, "lootTable", id);
        return new ChickenStickRecipe(id, input, lootTable);
    }

    @Override
    public ChickenStickRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        LootTable lootTable = readLootTable(buffer, id);
        return new ChickenStickRecipe(id, input, lootTable);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ChickenStickRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        writeLootTable(buffer, recipe.getLootTable());
    }

}
