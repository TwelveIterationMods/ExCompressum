package net.blay09.mods.excompressum.registry.compressor;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class CompressedRecipe {
    private final Ingredient ingredient;
    private final int count;
    private final ItemStack resultStack;

    public CompressedRecipe(Ingredient ingredient, int count, ItemStack resultStack) {
        this.ingredient = ingredient;
        this.count = count;
        this.resultStack = resultStack;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getCount() {
        return count;
    }

    public ItemStack getResultStack() {
        return resultStack;
    }
}
