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
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.recipeviewers.ExpandedWoodenCrucibleRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class WoodenCrucibleJeiRecipeCategory implements IRecipeCategory<ExpandedWoodenCrucibleRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "wooden_crucible");
    public static final RecipeType<ExpandedWoodenCrucibleRecipe> TYPE = new RecipeType<>(UID, ExpandedWoodenCrucibleRecipe.class);

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_wooden_crucible.png");

    private final IDrawable background;
    private final IDrawable icon;

    public WoodenCrucibleJeiRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.woodenCrucibles[0]));
    }

    @Override
    public RecipeType<ExpandedWoodenCrucibleRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, ExpandedWoodenCrucibleRecipe recipe, IFocusGroup focusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, 75, 10).addFluidStack(recipe.getFluid(), recipe.getAmount());

        final var inputs = recipe.getInputs();
        for (int i = 0; i < inputs.size(); i++) {
            final int slotX = 3 + (i % 9 * 18);
            final int slotY = 37 + (i / 9 * 18);
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, slotX, slotY).addItemStack(inputs.get(i));
        }
    }

}
