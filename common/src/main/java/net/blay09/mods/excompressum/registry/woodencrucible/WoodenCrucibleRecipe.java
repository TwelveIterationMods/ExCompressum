package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.registry.ExCompressumRecipe;
import net.blay09.mods.excompressum.registry.ModRecipeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

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

    public ResourceLocation getFluidId() {
        return fluid;
    }

    public void setFluidId(ResourceLocation fluid) {
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

    public boolean matchesFluid(Fluid fluid) {
        final var fluidId = Balm.getRegistries().getKey(fluid);
        return Objects.equals(fluidId, this.fluid);
    }

    public Fluid getFluid() {
        final var fluid = Balm.getRegistries().getFluid(this.fluid);
        return fluid != null ? fluid : Fluids.EMPTY;
    }
}
