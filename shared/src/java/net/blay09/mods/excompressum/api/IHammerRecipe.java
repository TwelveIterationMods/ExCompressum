package net.blay09.mods.excompressum.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;

public interface IHammerRecipe {

    Ingredient getInput();

    ILootTableProvider getLootTable();

    ResourceLocation getId();
}
