package net.blay09.mods.excompressum.forge.compat.jei;

import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CraftChickenStickRecipe {

	private final ItemStack input;
	private final ItemStack output;

	public CraftChickenStickRecipe() {
		input = new ItemStack(Items.STICK);
		output = new ItemStack(ModItems.chickenStick);
		CompoundTag tagCompound = new CompoundTag();
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
