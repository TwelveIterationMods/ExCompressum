package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.registry.CompressedRecipeRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotAutoCompressor extends Slot {
    public SlotAutoCompressor(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return CompressedRecipeRegistry.getRecipe(itemStack) != null;
    }
}
