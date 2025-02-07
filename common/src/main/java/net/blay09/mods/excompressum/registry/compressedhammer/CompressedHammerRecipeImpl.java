package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.LootTable;

public class CompressedHammerRecipeImpl extends ExCompressumRecipe implements CompressedHammerRecipe {

    private Ingredient ingredient;
    private LootTable lootTable;

    public CompressedHammerRecipeImpl(ResourceLocation id, Ingredient ingredient, LootTable lootTable) {
        super(id, ModRecipeTypes.compressedHammerRecipeType);
        this.ingredient = ingredient;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.compressedHammerRecipeSerializer;
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public LootTable getLootTable() {
        return lootTable;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setLootTable(LootTable lootTable) {
        this.lootTable = lootTable;
    }
}
