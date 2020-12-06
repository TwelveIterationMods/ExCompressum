package net.blay09.mods.excompressum.compat.jei;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.IRandomRange;

public class LootTableEntry {
    private final ItemStack itemStack;

    public LootTableEntry(ItemStack itemStack, IRandomRange countRange) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
