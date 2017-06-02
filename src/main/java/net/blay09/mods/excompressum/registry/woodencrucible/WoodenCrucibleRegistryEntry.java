package net.blay09.mods.excompressum.registry.woodencrucible;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class WoodenCrucibleRegistryEntry {
	private final ItemStack itemStack;
	private final Fluid fluid;
	private final int amount;

	public WoodenCrucibleRegistryEntry(ItemStack itemStack, Fluid fluid, int amount) {
		this.itemStack = itemStack;
		this.fluid = fluid;
		this.amount = amount;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public Fluid getFluid() {
		return fluid;
	}

	public int getAmount() {
		return amount;
	}

}
