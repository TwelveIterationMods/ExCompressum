package net.blay09.mods.excompressum.forge.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
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

public class HeavySieveRecipeCategory implements IRecipeCategory<JeiHeavySieveRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "heavy_sieve");
    public static final RecipeType<JeiHeavySieveRecipe> TYPE = new RecipeType<>(UID, JeiHeavySieveRecipe.class);
    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_heavy_sieve.png");

    private final IDrawable background;
    private final IDrawable icon;

    public HeavySieveRecipeCategory(IJeiHelpers jeiHelpers) {
        final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.heavySieves[0]));
    }

    @Override
    public RecipeType<JeiHeavySieveRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, JeiHeavySieveRecipe recipe, IFocusGroup focusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 61, 9);
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 87, 9);
        for (int i = 0; i < recipe.getOutputItems().size(); i++) {
            final int slotX = 2 + i * 18;
            final int slotY = 36;
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY);
        }
    }

}
