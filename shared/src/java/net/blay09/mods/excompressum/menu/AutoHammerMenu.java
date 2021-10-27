package net.blay09.mods.excompressum.menu;

import net.blay09.mods.excompressum.block.entity.AutoCompressedHammerBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AutoHammerMenu extends AbstractContainerMenu {

    private final AutoHammerBlockEntity autoHammer;

    private float lastProgress;
    private int lastEnergy;
    private boolean lastDisabledByRedstone;

    public AutoHammerMenu(int windowId, Inventory inventory, AutoHammerBlockEntity autoHammer) {
        super(ModMenus.autoHammer.get(), windowId);
        this.autoHammer = autoHammer;

        Container container = autoHammer.getBackingContainer();

        addSlot(new Slot(container, 0, 8, 35));

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

        addSlot(new AutoHammerUpgradeSlot(container, 21, 8, 62, autoHammer instanceof AutoCompressedHammerBlockEntity));
        addSlot(new AutoHammerUpgradeSlot(container, 22, 32, 62, autoHammer instanceof AutoCompressedHammerBlockEntity));
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        if (lastProgress != autoHammer.getProgress() || lastEnergy != autoHammer.getEnergyStorage().getEnergy() || lastDisabledByRedstone != autoHammer.isDisabledByRedstone()) {
            for (IContainerListener listener : listeners) {
                listener.sendWindowProperty(this, 0, (int) (100 * autoHammer.getProgress()));
                listener.sendWindowProperty(this, 1, autoHammer.getEnergyStorage().getEnergy());
                listener.sendWindowProperty(this, 2, autoHammer.isDisabledByRedstone() ? 1 : 0);
            }
        }
        lastProgress = autoHammer.getProgress();
        lastEnergy = autoHammer.getEnergyStorage().getEnergy();
        lastDisabledByRedstone = autoHammer.isDisabledByRedstone();
    }

    @Override
    public void updateProgressBar(int var, int val) {
        switch (var) {
            case 0:
                autoHammer.setProgress((float) val / 100f);
                break;
            case 1:
                autoHammer.getEnergyStorage().setEnergy(val);
                break;
            case 2:
                autoHammer.setDisabledByRedstone(val == 1);
                break;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return autoHammer.isUsableByPlayer(player);
    }

    @Override
    public ItemStack quickMoveStack(Player entityPlayer, int slotNumber) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = slots.get(slotNumber);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (slotNumber <= 20 || slotNumber >= 57) {
                if (!moveItemStackTo(slotStack, 21, 57, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (autoHammer.getBackingContainer().canPlaceItem(0, slotStack)) {
                if (!moveItemStackTo(slotStack, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (autoHammer.getBackingContainer().canPlaceItem(21, slotStack)) {
                if (!moveItemStackTo(slotStack, 57, 59, false)) {
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
            slot.onTake(entityPlayer, slotStack);
        }
        return itemStack;
    }

    public AutoHammerBlockEntity getAutoHammer() {
        return autoHammer;
    }
}
