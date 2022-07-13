package net.blay09.mods.excompressum.menu;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoHammerUpgradeSlot extends Slot {

    public AutoHammerUpgradeSlot(Container container, int index, int xPosition, int yPosition, boolean isCompressed) {
        super(container, index, xPosition, yPosition);
        ResourceLocation backgroundIcon = new ResourceLocation(ExCompressum.MOD_ID, isCompressed ? "items/empty_compressed_hammer_slot" : "items/empty_hammer_slot");
        setBackground(InventoryMenu.BLOCK_ATLAS, backgroundIcon);
    }

}
