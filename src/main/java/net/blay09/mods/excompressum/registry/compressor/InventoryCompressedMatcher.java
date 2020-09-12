package net.blay09.mods.excompressum.registry.compressor;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class InventoryCompressedMatcher extends CraftingInventory {

    private final ItemStack[] itemStacks;
    private final boolean isStupid;

    public InventoryCompressedMatcher(int width, int height, boolean isStupid) {
        super(new Container(null, 0) {
            @Override
            public boolean canInteractWith(PlayerEntity player) {
                return false;
            }
        }, width, height);
        this.itemStacks = new ItemStack[getSizeInventory()];
        this.isStupid = isStupid;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return i < getSizeInventory() ? itemStacks[i] : ItemStack.EMPTY;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        itemStacks[i] = itemStack;
    }

    public void fill(ItemStack itemStack) {
        if(isStupid) {
            Arrays.fill(itemStacks, ItemStack.EMPTY);
            itemStacks[0] = itemStack;
            itemStacks[1] = itemStack;
            itemStacks[3] = itemStack;
            itemStacks[4] = itemStack;
        } else {
            Arrays.fill(itemStacks, itemStack);
        }
    }
}
