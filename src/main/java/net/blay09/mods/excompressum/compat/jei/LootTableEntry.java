package net.blay09.mods.excompressum.compat.jei;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.IRandomRange;

public class LootTableEntry {
    private final ItemStack itemStack;
    private final IRandomRange countRange;
    private final float baseChance;

    public LootTableEntry(ItemStack itemStack, IRandomRange countRange, float baseChance) {
        this.itemStack = itemStack;
        this.countRange = countRange;
        this.baseChance = baseChance;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public IRandomRange getCountRange() {
        return countRange;
    }

    public float getBaseChance() {
        return baseChance;
    }
}
