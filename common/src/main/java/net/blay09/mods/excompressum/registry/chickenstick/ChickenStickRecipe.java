package net.blay09.mods.excompressum.registry.chickenstick;

import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.LootTable;

public class ChickenStickRecipe extends ExCompressumRecipe {

    private Ingredient input;
    private LootTable lootTable;

    public ChickenStickRecipe(ResourceLocation id, Ingredient input, LootTable lootTable) {
        super(id, ModRecipeTypes.chickenStickRecipeType);
        this.input = input;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.chickenStickRecipeSerializer;
    }

    public Ingredient getInput() {
        return input;
    }

    public LootTable getLootTable() {
        return lootTable;
    }

    public void setInput(Ingredient input) {
        this.input = input;
    }

    public void setLootTable(LootTable lootTable) {
        this.lootTable = lootTable;
    }
}
