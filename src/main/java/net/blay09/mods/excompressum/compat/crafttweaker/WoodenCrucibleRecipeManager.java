package net.blay09.mods.excompressum.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.crafttweaker.builder.ZenHeavySieveRecipe;
import net.blay09.mods.excompressum.compat.crafttweaker.builder.ZenWoodenCrucibleRecipe;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipe;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.WoodenCrucibleRecipes")
public class WoodenCrucibleRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public ZenWoodenCrucibleRecipe create(String recipeId) {
        recipeId = fixRecipeName(recipeId);
        ResourceLocation resourceLocation = new ResourceLocation(Compat.CRAFT_TWEAKER, recipeId);
        ZenWoodenCrucibleRecipe recipe = ZenWoodenCrucibleRecipe.builder(resourceLocation);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe.build(), ""));
        return recipe;
    }

    @Override
    public IRecipeType<WoodenCrucibleRecipe> getRecipeType() {
        return WoodenCrucibleRecipe.TYPE;
    }
}
