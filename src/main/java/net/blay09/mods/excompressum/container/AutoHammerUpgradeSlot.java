package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoHammerUpgradeSlot extends SlotItemHandler {

    public AutoHammerUpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition, boolean isCompressed) {
        super(itemHandler, index, xPosition, yPosition);
        ResourceLocation backgroundIcon = new ResourceLocation(ExCompressum.MOD_ID, isCompressed ? "items/empty_compressed_hammer_slot" : "items/empty_hammer_slot");
        setBackground(PlayerContainer.LOCATION_BLOCKS_TEXTURE, backgroundIcon);
    }

}
