package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ChickenStickRecipe implements IRecipeWrapper {

	private final List<ItemStack> inputs;
	private final List<ItemStack> outputs;

	public ChickenStickRecipe() {
		inputs = Lists.newArrayList(new ItemStack(Items.STICK));
		outputs = Lists.newArrayList(new ItemStack(ModItems.chickenStick));
	}

	@Override
	public List getInputs() {
		return inputs;
	}

	@Override
	public List getOutputs() {
		return outputs;
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return Collections.emptyList();
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return Collections.emptyList();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

	}

	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return Collections.emptyList();
	}

	@Override
	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}
}
