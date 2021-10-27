package net.blay09.mods.excompressum.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.crafttweaker.builder.ZenCompressedHammerRecipe;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.CompressedHammerRecipes")
public class CompressedHammerRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public ZenCompressedHammerRecipe create(String recipeId) {
        recipeId = fixRecipeName(recipeId);
        ResourceLocation resourceLocation = new ResourceLocation(Compat.CRAFT_TWEAKER, recipeId);
        ZenCompressedHammerRecipe recipe = ZenCompressedHammerRecipe.builder(resourceLocation);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe.build(), ""));
        return recipe;
    }

    @Override
    public RecipeType<CompressedHammerRecipe> getRecipeType() {
        return CompressedHammerRecipe.TYPE;
    }
}
