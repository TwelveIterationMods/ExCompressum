package net.blay09.mods.excompressum.menu;

import net.minecraft.resources.Identifier;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import org.jspecify.annotations.Nullable;

public class AutoHammerUpgradeSlot extends Slot {

    private final Identifier noItemIcon;

    public AutoHammerUpgradeSlot(Container container, int index, int xPosition, int yPosition, boolean isCompressed) {
        super(container, index, xPosition, yPosition);
        noItemIcon = Identifier.withDefaultNamespace(isCompressed ? "container/slot/compressed_hammer" : "container/slot/hammer");
    }

    @Override
    public Identifier getNoItemIcon() {
        return noItemIcon;
    }
}
