package net.blay09.mods.excompressum.registry.compressor;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class InventoryCompressedMatcher extends TransientCraftingContainer {

    private final ItemStack[] itemStacks;
    private final boolean isStupid;

    public InventoryCompressedMatcher(AbstractContainerMenu menu, int width, int height, boolean isStupid) {
        super(menu, width, height);
        this.itemStacks = new ItemStack[getContainerSize()];
        this.isStupid = isStupid;
    }

    @Override
    public ItemStack getItem(int i) {
        return i < getContainerSize() ? itemStacks[i] : ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
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
