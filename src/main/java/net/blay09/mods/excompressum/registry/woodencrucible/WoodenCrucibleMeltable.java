package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.registry.RegistryEntry;
import net.blay09.mods.excompressum.registry.TagOrResourceLocation;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class WoodenCrucibleMeltable extends RegistryEntry {
    private TagOrResourceLocation source;
    private ResourceLocation fluid;
    private int amount;

    public TagOrResourceLocation getSource() {
        return source;
    }

    public void setSource(TagOrResourceLocation source) {
        this.source = source;
    }

    public ResourceLocation getFluid() {
        return fluid;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public ResourceLocation getId() {
        return getSource().getResourceLocation();
    }

    public boolean matchesFluid(FluidStack fluid) {
        return Objects.equals(fluid.getFluid().getRegistryName(), this.fluid);
    }

    public FluidStack getFluidStack() {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(this.fluid);
        return new FluidStack(fluid, amount);
    }
}
