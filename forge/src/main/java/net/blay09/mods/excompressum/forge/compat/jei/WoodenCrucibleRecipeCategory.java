package net.blay09.mods.excompressum.forge.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class WoodenCrucibleRecipeCategory implements IRecipeCategory<JeiWoodenCrucibleRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "wooden_crucible");
    public static final RecipeType<JeiWoodenCrucibleRecipe> TYPE = new RecipeType<>(UID, JeiWoodenCrucibleRecipe.class);

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_wooden_crucible.png");

    private final IDrawable background;
    private final IDrawable icon;

    public WoodenCrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.woodenCrucibles[0]));
    }

    @Override
    public RecipeType<JeiWoodenCrucibleRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(UID.toString());
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, JeiWoodenCrucibleRecipe recipe, IFocusGroup focusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 61, 9);
    }

}
