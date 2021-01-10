package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.api.RegistryEntry;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class WoodenCrucibleMeltable extends RegistryEntry {
    private ResourceLocation id;
    private Ingredient source;
    private ResourceLocation fluid;
    private int amount;

    public Ingredient getSource() {
        return source;
    }

    public void setSource(Ingredient source) {
        this.source = source;
    }

    public ResourceLocation getFluid() {
        return fluid;
    }

    public void setFluid(ResourceLocation fluid) {
        this.fluid = fluid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public boolean matchesFluid(FluidStack fluid) {
        return Objects.equals(fluid.getFluid().getRegistryName(), this.fluid);
    }

    public FluidStack getFluidStack() {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(this.fluid);
        if(fluid != null) {
            return new FluidStack(fluid, amount);
        } else {
            return FluidStack.EMPTY;
        }
    }
}
