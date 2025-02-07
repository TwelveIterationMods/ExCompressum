package net.blay09.mods.excompressum.api.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.storage.loot.LootTable;

public interface HammerRecipe {

    Ingredient getIngredient();

    LootTable getLootTable();

    ResourceLocation getRecipeId();
}
