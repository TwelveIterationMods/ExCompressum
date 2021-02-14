package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class WoodenCrucibleRecipe extends ExCompressumRecipe {
    public static final IRecipeType<WoodenCrucibleRecipe> TYPE = IRecipeType.register(ExCompressum.MOD_ID + ":wooden_crucible");

    private Ingredient input;
    private ResourceLocation fluid;
    private int amount;

    public WoodenCrucibleRecipe(ResourceLocation id, Ingredient input, ResourceLocation fluid, int amount) {
        super(id, TYPE);
        this.input = input;
        this.fluid = fluid;
        this.amount = amount;
    }

    public Ingredient getInput() {
        return input;
    }

    public void setInput(Ingredient input) {
        this.input = input;
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

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.woodenCrucibleRecipe;
    }

    public boolean matchesFluid(FluidStack fluid) {
        return Objects.equals(fluid.getFluid().getRegistryName(), this.fluid);
    }

    public FluidStack getFluidStack() {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(this.fluid);
        if (fluid != null) {
            return new FluidStack(fluid, amount);
        } else {
            return FluidStack.EMPTY;
        }
    }
}
