package net.blay09.mods.excompressum.menu;

import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class AutoSieveMenu extends AbstractContainerMenu {

    private final AbstractAutoSieveBlockEntity autoSieve;

    public AutoSieveMenu(MenuType<AutoSieveMenu> type, int windowId, Inventory inventory, AbstractAutoSieveBlockEntity autoSieve) {
        super(type, windowId);
        this.autoSieve = autoSieve;

        Container container = autoSieve.getBackingContainer();

        addSlot(new Slot(container, 0, 8, 22));
        addSlot(new Slot(container, 21, 8, 49));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                addSlot(new OutputSlot(container, 1 + (i * 5) + j, 57 + (j * 18), 8 + (i * 18)));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inventory, i, 8 + i * 18, 142));
        }

        addDataSlots(autoSieve.getContainerData());
    }

    @Override
    public boolean stillValid(Player player) {
        return autoSieve.isUsableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotNumber) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotNumber);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotNumber <= 21) {
                if (!moveItemStackTo(slotStack, 22, 58, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (autoSieve.getBackingContainer().canPlaceItem(0, slotStack)) {
                if (!moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (autoSieve.getBackingContainer().canPlaceItem(21, slotStack)) {
                if (!moveItemStackTo(slotStack, 1, 2, false)) {
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

    public AbstractAutoSieveBlockEntity getAutoSieve() {
        return autoSieve;
    }
}
