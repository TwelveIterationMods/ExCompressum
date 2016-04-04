package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.tile.TileEntityAutoHammer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAutoHammer extends Slot {
    public SlotAutoHammer(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return ((TileEntityAutoHammer) inventory).isRegistered(itemStack);
    }
}
