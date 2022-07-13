package net.blay09.mods.excompressum.menu;

import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AutoCompressorMenu extends AbstractContainerMenu {

    private final AutoCompressorBlockEntity autoCompressor;

    public AutoCompressorMenu(int windowId, Inventory inventoryPlayer, AutoCompressorBlockEntity autoCompressor) {
        super(ModMenus.autoCompressor.get(), windowId);
        this.autoCompressor = autoCompressor;

        Container container = autoCompressor.getBackingContainer();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new Slot(container, i * 3 + j, 8 + (j * 18), 8 + (i * 18)));
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new OutputSlot(container, 12 + i * 3 + j, 93 + (j * 18), 8 + (i * 18)));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }

        addDataSlots(autoCompressor.getContainerData());
    }

    @Override
    public boolean stillValid(Player player) {
        return autoCompressor.isUsableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotNumber) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotNumber);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotNumber < 24) {
                if (!moveItemStackTo(slotStack, 24, 60, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (autoCompressor.getBackingContainer().canPlaceItem(0, slotStack)) {
                if (!moveItemStackTo(slotStack, 0, 12, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, slotStack);
        }
        return itemStack;
    }

    public AutoCompressorBlockEntity getAutoCompressor() {
        return autoCompressor;
    }
}
