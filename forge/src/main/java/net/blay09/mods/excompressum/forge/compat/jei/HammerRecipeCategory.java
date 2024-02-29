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

public class HammerRecipeCategory implements IRecipeCategory<JeiHammerRecipe> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_hammer.png");
    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "hammer");
    public static final RecipeType<JeiHammerRecipe> TYPE = new RecipeType<>(UID, JeiHammerRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public HammerRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 63);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.autoHammer));
    }

    @Override
    public RecipeType<JeiHammerRecipe> getRecipeType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return Component.translatable(UID.toString());
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, JeiHammerRecipe recipe, IFocusGroup focusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 74, 9);
        for (int i = 0; i < recipe.getOutputItems().size(); i++) {
            final int slotX = 2 + i * 18;
            final int slotY = 36;
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY);
        }
    }

}
