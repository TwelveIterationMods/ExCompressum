package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.woodencrucible.WoodenCrucibleRegistryEntry;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class WoodenCrucibleRecipe {

    private final Fluid fluid;
    private final List<WoodenCrucibleRegistryEntry> entries;
    private final List<ItemStack> inputs;

    public WoodenCrucibleRecipe(Fluid fluid, List<WoodenCrucibleRegistryEntry> entries) {
        this.fluid = fluid;
        this.entries = entries;

        inputs = Lists.newArrayList();
        for (WoodenCrucibleRegistryEntry entry : entries) {
            inputs.add(entry.getItemStack());
        }
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public FluidStack getFluidStack() {
        return new FluidStack(fluid, 1000);
    }

    public WoodenCrucibleRegistryEntry getEntryAt(int index) {
        return entries.get(index);
    }

}
