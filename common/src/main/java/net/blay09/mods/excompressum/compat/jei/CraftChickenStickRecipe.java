package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.component.ModComponents;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CraftChickenStickRecipe {

	private final ItemStack input = new ItemStack(Items.STICK);
	private final ItemStack output = ModItems.chickenStick.createStack();

	public CraftChickenStickRecipe() {
		input.set(ModComponents.angry.value(), Unit.INSTANCE);
	}

	public ItemStack getInput() {
		return input;
	}

	public ItemStack getOutput() {
		return output;
	}
}
