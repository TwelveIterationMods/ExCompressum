package net.blay09.mods.excompressum.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.blay09.mods.excompressum.tile.TileEntityAutoHammer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class SlotAutoHammerUpgrade extends Slot {
    public SlotAutoHammerUpgrade(IInventory inventory, int id, int x, int y) {
        super(inventory, id, x, y);
    }

    @Override
    public boolean isItemValid(ItemStack itemStack) {
        return ((TileEntityAutoHammer) inventory).isHammerUpgrade(itemStack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getBackgroundIconIndex() {
        return inventory instanceof TileEntityAutoCompressedHammer ? ClientProxy.iconEmptyCompressedHammerSlot : ClientProxy.iconEmptyHammerSlot;
    }
}
