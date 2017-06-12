package net.blay09.mods.excompressum.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ItemHandlerAutomation implements IItemHandlerModifiable {

	private final IItemHandlerModifiable base;

	public ItemHandlerAutomation(IItemHandlerModifiable base) {
		this.base = base;
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		base.setStackInSlot(slot, stack);
	}

	@Override
	public int getSlots() {
		return base.getSlots();
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return base.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if(!canInsertItem(slot, stack)) {
			return stack;
		}
		return base.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if(!canExtractItem(slot, amount)) {
			return ItemStack.EMPTY;
		}
		return base.extractItem(slot, amount, simulate);
	}

	public boolean canExtractItem(int slot, int amount) {
		return true;
	}

	public boolean canInsertItem(int slot, ItemStack itemStack) {
		return true;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

}
