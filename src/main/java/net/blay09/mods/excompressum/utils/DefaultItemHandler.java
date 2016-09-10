package net.blay09.mods.excompressum.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class DefaultItemHandler extends ItemStackHandler {

	public final TileEntity tileEntity;

	public DefaultItemHandler(TileEntity tileEntity, int size) {
		super(size);
		this.tileEntity = tileEntity;
	}

	@Override
	protected void onContentsChanged(int slot) {
		tileEntity.markDirty();
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if(!isItemValid(slot, stack)) {
			return stack;
		}
		return super.insertItem(slot, stack, simulate);
	}

	public boolean isItemValid(int slot, ItemStack itemStack) {
		return true;
	}
}
