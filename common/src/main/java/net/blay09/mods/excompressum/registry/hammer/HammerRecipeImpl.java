package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.storage.loot.LootTable;

public class HammerRecipeImpl extends ExCompressumRecipe implements HammerRecipe {

    private Ingredient ingredient;
    private LootTable lootTable;

    public HammerRecipeImpl(ResourceLocation id, Ingredient input, LootTable lootTable) {
        super(id, ModRecipeTypes.hammerRecipeType);
        this.ingredient = input;
        this.lootTable = lootTable;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.hammerRecipeSerializer;
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
