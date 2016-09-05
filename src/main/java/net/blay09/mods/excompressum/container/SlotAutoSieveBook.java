package net.blay09.mods.excompressum.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieveBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class SlotAutoSieveBook extends Slot {
    public SlotAutoSieveBook(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return ((TileEntityAutoSieveBase) inventory).isValidBook(itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getBackgroundIconIndex() {
        return ClientProxy.iconEmptyBookSlot;
    }
}
