package net.blay09.mods.excompressum.compat.jei;

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
import net.blay09.mods.excompressum.compat.recipeviewers.ExpandedSieveRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class SieveJeiRecipeCategory implements IRecipeCategory<ExpandedSieveRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "sieve");
    public static final RecipeType<ExpandedSieveRecipe> TYPE = new RecipeType<>(UID, ExpandedSieveRecipe.class);
    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_heavy_sieve.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SieveJeiRecipeCategory(IJeiHelpers jeiHelpers) {
        final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.autoSieve));
    }

    @Override
    public RecipeType<ExpandedSieveRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder recipeLayoutBuilder, ExpandedSieveRecipe recipe, IFocusGroup focusGroup) {
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 62, 10).addIngredients(recipe.getIngredient());
        recipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, 88, 10).addItemStacks(recipe.getMeshItems());
        final var outputItems = recipe.getOutputItems();
        for (int i = 0; i < outputItems.size(); i++) {
            final int slotX = 3 + (i % 9 * 18);
            final int slotY = 37 + (i / 9 * 18);
            recipeLayoutBuilder.addSlot(RecipeIngredientRole.OUTPUT, slotX, slotY).addItemStack(outputItems.get(i));
        }
    }

}
