package net.blay09.mods.excompressum.registry.compressor;

import net.minecraft.item.ItemStack;

public class CompressedRecipe {
    private final ItemStack sourceStack;
    private final ItemStack resultStack;

    public CompressedRecipe(ItemStack sourceStack, ItemStack resultStack) {
        this.sourceStack = sourceStack;
        this.resultStack = resultStack;
    }

    public ItemStack getSourceStack() {
        return sourceStack;
    }

    public ItemStack getResultStack() {
        return resultStack;
    }
}
