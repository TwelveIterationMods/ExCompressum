package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class CompressedHammerRecipe extends ExCompressumRecipe {


    private Ingredient input;
    private LootTableProvider lootTable;

    public CompressedHammerRecipe(ResourceLocation id, Ingredient input, LootTableProvider lootTable) {
        super(id, ModRecipeTypes.compressedHammerRecipeType);
        this.input = input;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.compressedHammerRecipeSerializer;
    }

    public Ingredient getInput() {
        return input;
    }

    public LootTableProvider getLootTable() {
        return lootTable;
    }

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public void setLootTable(LootTableProvider lootTable) {
        this.lootTable = lootTable;
    }
}
