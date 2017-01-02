package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ChickenStickRecipe extends BlankRecipeWrapper {

	private final ItemStack input;
	private final ItemStack output;

	public ChickenStickRecipe() {
		input = new ItemStack(Items.STICK);
		output = new ItemStack(ModItems.chickenStick);
		NBTTagCompound tagCompound = new NBTTagCompound();
		tagCompound.setBoolean("IsAngry", true);
		output.setTagCompound(tagCompound);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, input);
		ingredients.setOutput(ItemStack.class, output);
	}

}
