package net.blay09.mods.excompressum.compat.recipeviewers;

import com.mojang.datafixers.util.Pair;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;

import java.util.ArrayList;
import java.util.List;

public class ExpandedWoodenCrucibleRecipe {

    private final Fluid fluid;
    private final List<Pair<WoodenCrucibleRecipe, ItemStack>> entries;
    private final List<ItemStack> inputs;

    public ExpandedWoodenCrucibleRecipe(Fluid fluid, List<Pair<WoodenCrucibleRecipe, ItemStack>> entries) {
        this.fluid = fluid;
        this.entries = entries;

        inputs = new ArrayList<>();
        for (Pair<WoodenCrucibleRecipe, ItemStack> entry : entries) {
            inputs.add(entry.getSecond());
        }
    }

    public List<ItemStack> getInputs() {
        return inputs;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public int getAmount() {
        return 1000;
    }

    public WoodenCrucibleRecipe getEntryAt(int index) {
        return entries.get(index).getFirst();
    }

}
