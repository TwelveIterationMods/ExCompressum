package net.blay09.mods.excompressum.newregistry.chickenstick;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.LootTableProvider;
import net.blay09.mods.excompressum.newregistry.ExCompressumRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class ChickenStickRecipeSerializer extends ExCompressumRecipeSerializer<ChickenStickRecipe> {

    public ChickenStickRecipeSerializer() {
        setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick"));
    }

    @Override
    public ChickenStickRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
        LootTableProvider lootTable = readLootTableProvider(json, "lootTable");
        return new ChickenStickRecipe(id, input, lootTable);
    }

    @Override
    public ChickenStickRecipe read(ResourceLocation id, PacketBuffer buffer) {
        Ingredient input = Ingredient.read(buffer);
        LootTableProvider lootTable = readLootTableProvider(buffer);
        return new ChickenStickRecipe(id, input, lootTable);
    }

    @Override
    public void write(PacketBuffer buffer, ChickenStickRecipe recipe) {
        recipe.getInput().write(buffer);
        writeLootTable(buffer, recipe.getId(), recipe.getLootTable());
    }

}
