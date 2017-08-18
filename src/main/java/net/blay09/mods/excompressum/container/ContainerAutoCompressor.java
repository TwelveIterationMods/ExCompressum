package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.tile.TileAutoCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerAutoCompressor extends Container {

    private final TileAutoCompressor tileEntity;

    private float lastProgress;
    private int lastEnergy;
    private boolean lastDisabledByRedstone;

    public ContainerAutoCompressor(InventoryPlayer inventoryPlayer, TileAutoCompressor tileEntity) {
        this.tileEntity = tileEntity;

        ItemStackHandler itemHandler = tileEntity.getItemHandler();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotItemHandler(itemHandler, i * 3 + j, 8 + (j * 18), 8 + (i * 18)));
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotOutput(itemHandler, 12 + i * 3 + j, 93 + (j * 18), 8 + (i * 18)));
            }
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(inventoryPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 142));
        }
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if(tileEntity.getProgress() != lastProgress || tileEntity.getEnergyStorage().getEnergyStored() != lastEnergy || tileEntity.isDisabledByRedstone()) {
            for (IContainerListener listener : listeners) {
                listener.sendWindowProperty(this, 0, (int) (100 * tileEntity.getProgress()));
                listener.sendWindowProperty(this, 1, tileEntity.getEnergyStorage().getEnergyStored());
                listener.sendWindowProperty(this, 2, tileEntity.isDisabledByRedstone() ? 1 : 0);
            }
        }
        lastProgress = tileEntity.getProgress();
        lastEnergy = tileEntity.getEnergyStorage().getEnergyStored();
        lastDisabledByRedstone = tileEntity.isDisabledByRedstone();
    }

    @Override
    public void updateProgressBar(int var, int val) {
        switch(var) {
            case 0: tileEntity.setProgress((float) val / 100f); break;
            case 1: tileEntity.getEnergyStorage().setEnergyStored(val); break;
            case 2: tileEntity.setDisabledByRedstone(val == 1); break;
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tileEntity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotNumber) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotNumber);
        if(slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if(slotNumber < 24) {
                if (!mergeItemStack(slotStack, 24, 60, true)) {
                    return ItemStack.EMPTY;
                }
            } else if(tileEntity.getItemHandler().isItemValid(0, slotStack)) {
                if (!mergeItemStack(slotStack, 0, 12, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if(slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if(slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(entityPlayer, slotStack);
        }
        return itemStack;
    }
}
