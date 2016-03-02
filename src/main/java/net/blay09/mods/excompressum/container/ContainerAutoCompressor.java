package net.blay09.mods.excompressum.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.blay09.mods.excompressum.registry.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerAutoCompressor extends Container {

    private final TileEntityAutoCompressor tileEntity;
    private int lastEnergy;

    public ContainerAutoCompressor(InventoryPlayer inventoryPlayer, TileEntityAutoCompressor tileEntity) {
        this.tileEntity = tileEntity;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotAutoCompressor(tileEntity, i * 3 + j, 8 + (j * 18), 8 + (i * 18)));
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                addSlotToContainer(new SlotOutput(tileEntity, 12 + i * 3 + j, 93 + (j * 18), 8 + (i * 18)));
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
        int energyStored = tileEntity.getEnergyStored(null);
        if(lastEnergy != energyStored) {
            for (Object obj : crafters) {
                ((ICrafting) obj).sendProgressBarUpdate(this, 0, energyStored);
            }
            lastEnergy = energyStored;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int index, int value) {
        tileEntity.setEnergyStored(value);
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityPlayer) {
        return tileEntity.isUseableByPlayer(entityPlayer);
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer entityPlayer, int slotNumber) {
        ItemStack itemStack = null;
        Slot slot = getSlot(slotNumber);
        if(slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();
            if(slotNumber < 24) {
                if (!mergeItemStack(slotStack, 24, 60, true)) {
                    return null;
                }
            } else if(CompressedRecipeRegistry.getRecipe(slotStack) != null) {
                if (!mergeItemStack(slotStack, 0, 12, false)) {
                    return null;
                }
            }
            if(slotStack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            if(slotStack.stackSize == itemStack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(entityPlayer, slotStack);
        }
        return itemStack;
    }
}
