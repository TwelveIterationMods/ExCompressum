package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.blay09.mods.excompressum.api.woodencrucible.WoodenCrucibleRegistryEntry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collections;
import java.util.List;

public class WoodenCrucibleRecipe implements IRecipeWrapper {

    private final Fluid fluid;
    private final List<WoodenCrucibleRegistryEntry> entries;
    private final List<ItemStack> inputs;
    private final List<FluidStack> fluidOutputs;

    public WoodenCrucibleRecipe(Fluid fluid, List<WoodenCrucibleRegistryEntry> entries) {
        this.fluid = fluid;
        this.entries = entries;

        inputs = Lists.newArrayList();
        for (WoodenCrucibleRegistryEntry entry : entries) {
            inputs.add(entry.getItemStack());
        }

        fluidOutputs = Collections.singletonList(new FluidStack(fluid, Fluid.BUCKET_VOLUME));
    }

    public Fluid getFluid() {
        return fluid;
    }

    public WoodenCrucibleRegistryEntry getEntryAt(int index) {
        return entries.get(index);
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInputs(ItemStack.class, inputs);
        ingredients.setOutputs(FluidStack.class, fluidOutputs);
    }

}
