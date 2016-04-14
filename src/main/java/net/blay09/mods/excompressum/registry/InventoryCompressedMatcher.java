package net.blay09.mods.excompressum.registry;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryCompressedMatcher extends InventoryCrafting {

    private final ItemStack[] itemStacks;
    private final boolean isStupid;

    public InventoryCompressedMatcher(int width, int height, boolean isStupid) {
        super(null, width, height);
        this.itemStacks = new ItemStack[getSizeInventory()];
        this.isStupid = isStupid;
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
        if(isStupid) {
            for (int i = 0; i < itemStacks.length; i++) {
                itemStacks[i] = null;
            }
            itemStacks[0] = itemStack;
            itemStacks[1] = itemStack;
            itemStacks[3] = itemStack;
            itemStacks[4] = itemStack;
        } else {
            for (int i = 0; i < itemStacks.length; i++) {
                itemStacks[i] = itemStack;
            }
        }
    }
}
