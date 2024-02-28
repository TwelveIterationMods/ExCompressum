package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class WoodenCrucibleRecipe extends ExCompressumRecipe {

    private Ingredient input;
    private ResourceLocation fluid;
    private int amount;

    public WoodenCrucibleRecipe(ResourceLocation id, Ingredient input, ResourceLocation fluid, int amount) {
        super(id, ModRecipeTypes.woodenCrucibleRecipeType);
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
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeTypes.woodenCrucibleRecipeSerializer;
    }

    public boolean matchesFluid(FluidStack fluid) {
        final var fluidId = Balm.getRegistries().getKey(fluid.getFluid());
        return Objects.equals(fluidId, this.fluid);
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
