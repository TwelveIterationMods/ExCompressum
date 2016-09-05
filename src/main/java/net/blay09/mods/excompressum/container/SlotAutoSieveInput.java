package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.tile.TileEntityAutoSieveBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAutoSieveInput extends Slot {
    public SlotAutoSieveInput(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return ((TileEntityAutoSieveBase) inventory).isRegistered(itemStack);
    }
}
