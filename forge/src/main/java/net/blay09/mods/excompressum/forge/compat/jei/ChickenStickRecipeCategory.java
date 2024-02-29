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
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ChickenStickRecipeCategory implements IRecipeCategory<JeiChickenStickRecipe> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_hammer.png");
    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick");
    public static final RecipeType<JeiChickenStickRecipe> TYPE = new RecipeType<>(UID, JeiChickenStickRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ChickenStickRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 63);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.chickenStick));
    }

    @Override
    public RecipeType<JeiChickenStickRecipe> getRecipeType() {
        return TYPE;
    }

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
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, JeiChickenStickRecipe recipe, IFocusGroup focusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 74, 9);

        final var outputItems = recipe.getOutputItems();
        for (int i = 0; i < outputItems.size(); i++) {
            final int slotX = 2 + i * 18;
            final int slotY = 36;
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY);
        }
    }

}
