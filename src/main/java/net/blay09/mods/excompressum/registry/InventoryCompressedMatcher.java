package net.blay09.mods.excompressum.registry;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryCompressedMatcher extends InventoryCrafting {

    private final ItemStack[] itemStacks;

    public InventoryCompressedMatcher(int width, int height) {
        super(null, width, height);
        this.itemStacks = new ItemStack[getSizeInventory()];
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return i < getSizeInventory() ? itemStacks[i] : null;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        itemStacks[i] = itemStack;
    }

    public void fill(ItemStack itemStack) {
        for(int i = 0; i < itemStacks.length; i++) {
            itemStacks[i] = itemStack;
        }
    }
}
