package net.blay09.mods.excompressum.registry.hammer;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

public class HammerRecipeSerializer extends ExCompressumRecipeSerializer<HammerRecipeImpl> {

    @Override
    public HammerRecipeImpl readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        LootTable lootTable = readLootTable(json, "lootTable", id);
        return new HammerRecipeImpl(id, input, lootTable);
    }

    @Override
    public HammerRecipeImpl fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        LootTable lootTable = readLootTable(buffer, id);
        return new HammerRecipeImpl(id, input, lootTable);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, HammerRecipeImpl recipe) {
        recipe.getIngredient().toNetwork(buffer);
        writeLootTable(buffer, recipe.getLootTable());
    }

}
