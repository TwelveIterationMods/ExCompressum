package net.blay09.mods.excompressum.menu;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;

public class AutoHammerUpgradeSlot extends Slot {

    private final ResourceLocation noItemIconSprite;
    private final Pair<ResourceLocation, ResourceLocation> noItemIcon;

    public AutoHammerUpgradeSlot(Container container, int index, int xPosition, int yPosition, boolean isCompressed) {
        super(container, index, xPosition, yPosition);
        noItemIconSprite = new ResourceLocation("minecraft",
                isCompressed ? "excompressum_icons/empty_compressed_hammer_slot" : "excompressum_icons/empty_hammer_slot");
        noItemIcon = Pair.of(InventoryMenu.BLOCK_ATLAS, noItemIconSprite);
    }

    @Nullable
    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return noItemIcon;
    }
}
