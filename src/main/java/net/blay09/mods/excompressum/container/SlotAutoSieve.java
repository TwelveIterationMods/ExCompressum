package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.tile.TileEntityAutoSieve;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAutoSieve extends Slot {
    public SlotAutoSieve(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return ((TileEntityAutoSieve) inventory).isRegistered(itemStack);
    }
}
