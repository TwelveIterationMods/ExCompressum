package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.registry.CompressedHammerRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAutoCompressedHammer extends Slot {
    public SlotAutoCompressedHammer(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return CompressedHammerRegistry.isRegistered(itemStack);
    }
}
