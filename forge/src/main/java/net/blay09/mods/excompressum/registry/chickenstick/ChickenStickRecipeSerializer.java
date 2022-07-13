package net.blay09.mods.excompressum.registry.chickenstick;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.ExCompressumRecipeSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;

public class ChickenStickRecipeSerializer extends ExCompressumRecipeSerializer<ChickenStickRecipe> {

    public ChickenStickRecipeSerializer() {
        setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick"));
    }

    @Override
    public ChickenStickRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "input"));
        LootTableProvider lootTable = readLootTableProvider(json, "lootTable");
        return new ChickenStickRecipe(id, input, lootTable);
    }

    @Override
    public ChickenStickRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        LootTableProvider lootTable = readLootTableProvider(buffer);
        return new ChickenStickRecipe(id, input, lootTable);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, ChickenStickRecipe recipe) {
        recipe.getInput().toNetwork(buffer);
        writeLootTable(buffer, recipe.getId(), recipe.getLootTable());
    }

}
