package net.blay09.mods.excompressum.api;

import net.minecraft.item.crafting.Ingredient;

public interface IHammerRecipe {

    Ingredient getInput();

    ILootTableProvider getLootTable();
}
