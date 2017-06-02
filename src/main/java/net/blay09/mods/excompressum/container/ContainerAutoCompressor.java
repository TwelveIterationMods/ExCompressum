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

        if(tileEntity.getProgress() != lastProgress || tileEntity.getEnergyStorage().getEnergyStored() != lastEnergy) {
            for (IContainerListener listener : listeners) {
                listener.sendProgressBarUpdate(this, 0, (int) (100 * tileEntity.getProgress()));
                listener.sendProgressBarUpdate(this, 1, tileEntity.getEnergyStorage().getEnergyStored());
            }
        }
        lastProgress = tileEntity.getProgress();
        lastEnergy = tileEntity.getEnergyStorage().getEnergyStored();
    }

    @Override
    public void updateProgressBar(int var, int val) {
        switch(var) {
            case 0: tileEntity.setProgress((float) val / 100f);
            case 1: tileEntity.getEnergyStorage().setEnergyStored(val);
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
        ItemStack slotStack = slot != null ? slot.getStack() : ItemStack.EMPTY;
        if(!slotStack.isEmpty()) {
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
            if(slotStack.getCount() == 0) {
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
