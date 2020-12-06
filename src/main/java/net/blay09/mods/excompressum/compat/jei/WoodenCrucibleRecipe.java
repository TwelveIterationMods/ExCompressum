package net.blay09.mods.excompressum.compat.jei;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleMeltable;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class WoodenCrucibleRecipe {

    private final Fluid fluid;
    private final List<Pair<WoodenCrucibleMeltable, ItemStack>> entries;
    private final List<ItemStack> inputs;

    public WoodenCrucibleRecipe(Fluid fluid, List<Pair<WoodenCrucibleMeltable, ItemStack>> entries) {
        this.fluid = fluid;
        this.entries = entries;

        inputs = new ArrayList<>();
        for (Pair<WoodenCrucibleMeltable, ItemStack> entry : entries) {
            inputs.add(entry.getSecond());
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

    public WoodenCrucibleMeltable getEntryAt(int index) {
        return entries.get(index).getFirst();
    }

}
