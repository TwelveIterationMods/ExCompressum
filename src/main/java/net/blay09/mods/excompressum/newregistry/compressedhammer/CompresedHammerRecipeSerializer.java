package net.blay09.mods.excompressum.newregistry.compressedhammer;

import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.LootTableProvider;
import net.blay09.mods.excompressum.newregistry.ExCompressumRecipeSerializer;
import net.blay09.mods.excompressum.newregistry.heavysieve.GeneratedHeavySieveRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;

public class CompresedHammerRecipeSerializer extends ExCompressumRecipeSerializer<CompressedHammerRecipe> {

    public CompresedHammerRecipeSerializer() {
        setRegistryName(new ResourceLocation(ExCompressum.MOD_ID, "compressed_hammer"));
    }

    @Override
    public CompressedHammerRecipe readFromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.deserialize(JSONUtils.getJsonObject(json, "input"));
        LootTableProvider lootTable = readLootTableProvider(json, "lootTable");
        return new CompressedHammerRecipe(id, input, lootTable);
    }

    @Override
    public CompressedHammerRecipe read(ResourceLocation id, PacketBuffer buffer) {
        Ingredient input = Ingredient.read(buffer);
        LootTableProvider lootTable = readLootTableProvider(buffer);
        return new CompressedHammerRecipe(id, input, lootTable);
    }

    @Override
    public void write(PacketBuffer buffer, CompressedHammerRecipe recipe) {
        recipe.getInput().write(buffer);
        writeLootTable(buffer, recipe.getId(), recipe.getLootTable());
    }

}
