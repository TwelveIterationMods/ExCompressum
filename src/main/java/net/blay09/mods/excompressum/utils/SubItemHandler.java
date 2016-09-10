package net.blay09.mods.excompressum.utils;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RangedWrapper;

public class SubItemHandler extends RangedWrapper {
	private final int minSlot;
	private final int maxSlot;

	public SubItemHandler(IItemHandlerModifiable compose, int minSlot, int maxSlotExclusive) {
		super(compose, minSlot, maxSlotExclusive);
		this.minSlot = minSlot;
		this.maxSlot = maxSlotExclusive;
	}

	public boolean isInside(int slot)
	{
		return slot >= minSlot && slot < maxSlot;
	}

}
