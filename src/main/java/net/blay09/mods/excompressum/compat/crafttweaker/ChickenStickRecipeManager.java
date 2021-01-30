package net.blay09.mods.excompressum.compat.crafttweaker;

import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.blamejared.crafttweaker.api.annotations.ZenRegister;
import com.blamejared.crafttweaker.api.managers.IRecipeManager;
import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.crafttweaker.builder.ZenChickenStickRecipe;
import net.blay09.mods.excompressum.compat.crafttweaker.builder.ZenHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import org.openzen.zencode.java.ZenCodeType;

@ZenRegister
@ZenCodeType.Name("mods.excompressum.ChickenStickRecipes")
public class ChickenStickRecipeManager implements IRecipeManager {

    @ZenCodeType.Method
    public ZenChickenStickRecipe create(String recipeId) {
        recipeId = fixRecipeName(recipeId);
        ResourceLocation resourceLocation = new ResourceLocation(Compat.CRAFT_TWEAKER, recipeId);
        ZenChickenStickRecipe recipe = ZenChickenStickRecipe.builder(resourceLocation);
        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe.build(), ""));
        return recipe;
    }

    @Override
    public IRecipeType<ChickenStickRecipe> getRecipeType() {
        return ChickenStickRecipe.TYPE;
    }
}
