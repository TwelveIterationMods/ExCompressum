package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CraftChickenStickRecipe {

	private final ItemStack input = new ItemStack(Items.STICK);
	private final ItemStack output = new ItemStack(ModItems.chickenStick);

	public CraftChickenStickRecipe() {
		final var tagCompound = new CompoundTag();
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
