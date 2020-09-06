package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.tile.AutoCompressedHammerTileEntity;
import net.blay09.mods.excompressum.tile.AutoHammerTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoHammerContainer extends Container {

    private final AutoHammerTileEntity tileEntity;

    private float lastProgress;
    private int lastEnergy;
    private boolean lastDisabledByRedstone;

    public AutoHammerContainer(int windowId, PlayerInventory inventoryPlayer, AutoHammerTileEntity tileEntity) {
        super(ModContainers.autoHammer, windowId);
        this.tileEntity = tileEntity;

        ItemStackHandler itemHandler = tileEntity.getItemHandler();

        addSlot(new SlotItemHandler(itemHandler, 0, 8, 35));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                addSlot(new SlotOutput(itemHandler, 1 + (i * 5) + j, 57 + (j * 18), 8 + (i * 18)));
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

        addSlot(new SlotAutoHammerUpgrade(itemHandler, 21, 8, 62, tileEntity instanceof AutoCompressedHammerTileEntity));
        addSlot(new SlotAutoHammerUpgrade(itemHandler, 22, 32, 62, tileEntity instanceof AutoCompressedHammerTileEntity));
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        if(lastProgress != tileEntity.getProgress() || lastEnergy != tileEntity.getEnergyStorage().getEnergyStored() || lastDisabledByRedstone != tileEntity.isDisabledByRedstone()) {
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
    public boolean canInteractWith(PlayerEntity entityPlayer) {
        return tileEntity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity entityPlayer, int slotNumber) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(slotNumber);
        if(slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if(slotNumber <= 20 || slotNumber >= 57) {
                if(!mergeItemStack(slotStack, 21, 57, true)) {
                    return ItemStack.EMPTY;
                }
            } else if(tileEntity.getItemHandler().isItemValid(0, slotStack)) {
                if (!mergeItemStack(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if(tileEntity.getItemHandler().isItemValid(21, slotStack)) {
                if (!mergeItemStack(slotStack, 57, 59, false)) {
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
