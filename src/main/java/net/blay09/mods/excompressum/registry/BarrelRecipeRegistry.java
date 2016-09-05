package net.blay09.mods.excompressum.registry;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public class BarrelRecipeRegistry {
	@Nullable
	public static ItemStack getOutput(FluidStack fluidStack, ItemStack itemStack) {
		return Math.random() > 0.5 ? new ItemStack(Items.STICK) : null;
	}
}
