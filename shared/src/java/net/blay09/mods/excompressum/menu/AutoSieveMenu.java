package net.blay09.mods.excompressum.menu;

import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoSieveMenu extends AbstractContainerMenu {

    private final AbstractAutoSieveBlockEntity tileEntity;

    private float lastProgress;
    private int lastEnergy;
    private boolean lastDisabledByRedstone;

    public AutoSieveMenu(MenuType<AutoSieveMenu> type, int windowId, Inventory inventory, AbstractAutoSieveBlockEntity tileEntity) {
        super(type, windowId);
        this.tileEntity = tileEntity;

        Container container = tileEntity.getBackingContainer();

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
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (lastProgress != tileEntity.getProgress() || lastEnergy != tileEntity.getEnergyStored() || lastDisabledByRedstone != tileEntity.isDisabledByRedstone()) {
            for (IContainerListener listener : listeners) {
                listener.sendWindowProperty(this, 0, (int) (100 * tileEntity.getProgress()));
                listener.sendWindowProperty(this, 1, tileEntity.getEnergyStored());
                listener.sendWindowProperty(this, 2, tileEntity.isDisabledByRedstone() ? 1 : 0);
            }
        }
        lastProgress = tileEntity.getProgress();
        lastEnergy = tileEntity.getEnergyStored();
        lastDisabledByRedstone = tileEntity.isDisabledByRedstone();
    }

    @Override
    public void updateProgressBar(int var, int val) {
        switch (var) {
            case 0:
                tileEntity.setProgress((float) val / 100f);
                break;
            case 1:
                tileEntity.setEnergyStored(val);
                break;
            case 2:
                tileEntity.setDisabledByRedstone(val == 1);
                break;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return tileEntity.isUsableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int slotNumber) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotNumber);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotNumber <= 21) {
                if (!moveItemStackTo(slotStack, 22, 57, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getBackingContainer().canPlaceItem(0, slotStack)) {
                if (!moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getBackingContainer().canPlaceItem(21, slotStack)) {
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

    public AbstractAutoSieveBlockEntity getTileEntity() {
        return tileEntity;
    }
}
