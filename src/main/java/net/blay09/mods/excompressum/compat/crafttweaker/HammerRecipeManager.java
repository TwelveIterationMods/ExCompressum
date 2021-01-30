package net.blay09.mods.excompressum.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.crafttweaker.builder.ZenHammerRecipe;
import net.blay09.mods.excompressum.compat.crafttweaker.builder.ZenHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipe;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.HammerRecipes")
public class HammerRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public ZenHammerRecipe create(String recipeId) {
        recipeId = fixRecipeName(recipeId);
        ResourceLocation resourceLocation = new ResourceLocation(Compat.CRAFT_TWEAKER, recipeId);
        ZenHammerRecipe recipe = ZenHammerRecipe.builder(resourceLocation);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe.build(), ""));
        return recipe;
    }

    @Override
    public IRecipeType<HammerRecipe> getRecipeType() {
        return HammerRecipe.TYPE;
    }
}
