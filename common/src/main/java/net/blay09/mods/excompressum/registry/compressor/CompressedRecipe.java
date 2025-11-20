package net.blay09.mods.excompressum.registry.compressor;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public record CompressedRecipe(Identifier id, Ingredient ingredient, int count, ItemStack resultStack) {
}
