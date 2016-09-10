package net.blay09.mods.excompressum.registry.woodencrucible;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.oredict.OreDictionary;

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

	public String getKey() {
		String registryName = itemStack.getItem().getRegistryName().toString();
		if(itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
			return registryName + ":*";
		} else {
			return registryName + ":" + itemStack.getItemDamage();
		}
	}
}
