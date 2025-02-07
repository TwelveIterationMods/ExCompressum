package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.recipeviewers.ExpandedChickenStickRecipe;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ChickenStickJeiRecipeCategory implements IRecipeCategory<ExpandedChickenStickRecipe> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_hammer.png");
    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick");
    public static final RecipeType<ExpandedChickenStickRecipe> TYPE = new RecipeType<>(UID, ExpandedChickenStickRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public ChickenStickJeiRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 63);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.chickenStick));
    }

    @Override
    public RecipeType<ExpandedChickenStickRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, ExpandedChickenStickRecipe recipe, IFocusGroup focusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 75, 10).addIngredients(recipe.getIngredient());

        final var outputItems = recipe.getOutputItems();
        for (int i = 0; i < outputItems.size(); i++) {
            final int slotX = 3 + i * 18;
            final int slotY = 37;
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addItemStack(outputItems.get(i));
        }
    }

}
