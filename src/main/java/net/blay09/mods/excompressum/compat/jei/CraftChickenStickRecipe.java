package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;

public class CraftChickenStickRecipe {

	private final ItemStack input;
	private final ItemStack output;

	public CraftChickenStickRecipe() {
		input = new ItemStack(Items.STICK);
		output = new ItemStack(ModItems.chickenStick);
		CompoundNBT tagCompound = new CompoundNBT();
		tagCompound.putBoolean("IsAngry", true);
		output.setTag(tagCompound);
	}

	public ItemStack getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output;
	}
}
