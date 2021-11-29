package net.blay09.mods.excompressum.api;

import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

public interface IHammerRecipe {

    Ingredient getInput();

    ILootTableProvider getLootTable();

    ResourceLocation getRecipeId();
}
