package net.blay09.mods.excompressum.menu;

import net.blay09.mods.excompressum.block.entity.AutoCompressorBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoCompressorMenu extends AbstractContainerMenu {

    private final AutoCompressorBlockEntity tileEntity;

    private float lastProgress;
    private int lastEnergy;
    private boolean lastDisabledByRedstone;

    public AutoCompressorMenu(int windowId, Inventory inventoryPlayer, AutoCompressorBlockEntity tileEntity) {
        super(ModMenus.autoCompressor.get(), windowId);
        this.tileEntity = tileEntity;

        Container container = tileEntity.getBackingContainer();

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
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (tileEntity.getProgress() != lastProgress || tileEntity.getEnergyStorage().getEnergy() != lastEnergy || tileEntity.isDisabledByRedstone()) {
            for (IContainerListener listener : listeners) {
                listener.sendWindowProperty(this, 0, (int) (100 * tileEntity.getProgress()));
                listener.sendWindowProperty(this, 1, tileEntity.getEnergyStorage().getEnergy());
                listener.sendWindowProperty(this, 2, tileEntity.isDisabledByRedstone() ? 1 : 0);
            }
        }
        lastProgress = tileEntity.getProgress();
        lastEnergy = tileEntity.getEnergyStorage().getEnergy();
        lastDisabledByRedstone = tileEntity.isDisabledByRedstone();
    }

    @Override
    public void updateProgressBar(int var, int val) {
        switch (var) {
            case 0:
                tileEntity.setProgress((float) val / 100f);
                break;
            case 1:
                tileEntity.getEnergyStorage().setEnergy(val);
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
            if (slotNumber < 24) {
                if (!moveItemStackTo(slotStack, 24, 60, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (tileEntity.getBackingContainer().canPlaceItem(0, slotStack)) {
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

    public AutoCompressorBlockEntity getTileEntity() {
        return tileEntity;
    }
}
