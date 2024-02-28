package net.blay09.mods.excompressum.forge.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class CraftChickenStickRecipeCategory implements IRecipeCategory<CraftChickenStickRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "craft_chicken_stick");
    public static final RecipeType<CraftChickenStickRecipe> TYPE = new RecipeType<>(UID, CraftChickenStickRecipe.class);
    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_chicken_stick.png");

    private final IDrawable background;
    private final IDrawable icon;

    public CraftChickenStickRecipeCategory(IGuiHelper guiHelper) {
        background = guiHelper.createDrawable(texture, 0, 0, 166, 58);
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.chickenStick));
    }

    @Override
    public RecipeType<CraftChickenStickRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable("item.excompressum.chicken_stick");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, CraftChickenStickRecipe craftChickenStickRecipe, IFocusGroup iFocusGroup) {
        // TODO
    }

    /*@Override
    public void setIngredients(CraftChickenStickRecipe chickenStickRecipe, IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, chickenStickRecipe.getInput());
        ingredients.setOutput(VanillaTypes.ITEM, chickenStickRecipe.getOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CraftChickenStickRecipe recipeWrapper, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 16, 20);
        recipeLayout.getItemStacks().init(1, false, 130, 20);
        recipeLayout.getItemStacks().set(ingredients);
    }*/

}
