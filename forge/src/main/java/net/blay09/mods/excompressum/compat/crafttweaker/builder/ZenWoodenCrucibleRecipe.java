package net.blay09.mods.excompressum.compat.crafttweaker.builder;


import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.fluid.IFluidStack;
import com.blamejared.crafttweaker.api.ingredient.IIngredient;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.WoodenCrucibleRecipe")
public class ZenWoodenCrucibleRecipe {

    private final WoodenCrucibleRecipe recipe;

    private ZenWoodenCrucibleRecipe(ResourceLocation recipeId) {
        this.recipe = new WoodenCrucibleRecipe(recipeId, Ingredient.EMPTY, new ResourceLocation("water"), 100);
    }

    @ZenCodeType.Method
    public static ZenWoodenCrucibleRecipe builder(ResourceLocation recipeId) {
        return new ZenWoodenCrucibleRecipe(recipeId);
    }

    @ZenCodeType.Method
    public ZenWoodenCrucibleRecipe setFluid(IFluidStack fluid) {
        recipe.setFluid(fluid.getRegistryName());
        recipe.setAmount(fluid.getAmount());
        return this;
    }

    @ZenCodeType.Method
    public ZenWoodenCrucibleRecipe setInput(IIngredient input) {
        recipe.setInput(input.asVanillaIngredient());
        return this;
    }

    public WoodenCrucibleRecipe build() {
        return recipe;
    }

}
