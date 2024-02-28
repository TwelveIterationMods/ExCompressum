package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
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
    private final IDrawable slotHighlight;
    private final IDrawable icon;
    // TODO private final IDrawable water;
    private final IJeiHelpers jeiHelpers;
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public HeavySieveRecipeCategory(IJeiHelpers jeiHelpers) {
        this.jeiHelpers = jeiHelpers;
        final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
        this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.heavySieves[0]));
        // TODO water = jeiHelpers.getGuiHelper().createDrawableIngredient(VanillaTypes.FLUID_STACK, new FluidStack(Fluids.WATER, 1000));
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, JeiHeavySieveRecipe jeiHeavySieveRecipe, IFocusGroup iFocusGroup) {
        // TODO
    }

    /*@Override
    public void draw(JeiHeavySieveRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        if (hasHighlight) {
            slotHighlight.draw(poseStack, highlightX, highlightY);
        }
        if (recipe.isWaterlogged()) {
            water.draw(poseStack, 62, 10);
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, final JeiHeavySieveRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 61, 9);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        recipeLayout.getItemStacks().init(1, true, 87, 9);
        recipeLayout.getItemStacks().set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));

        IFocus<ItemStack> focus = recipeLayout.getFocus(VanillaTypes.ITEM);
        hasHighlight = focus != null && focus.getMode() == IFocus.Mode.OUTPUT;
        final List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
        final int INPUT_SLOTS = 2;
        int slotNumber = 0;
        for (List<ItemStack> output : outputs) {
            final int slotX = 2 + (slotNumber % 9 * 18);
            final int slotY = 36 + (slotNumber / 9 * 18);
            recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, false, slotX, slotY);
            recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, output);
            if (focus != null) {
                ItemStack focusStack = focus.getValue();
                if (focus.getMode() == IFocus.Mode.OUTPUT) {
                    if (focusStack.getItem() == output.get(0).getItem()) {
                        highlightX = slotX;
                        highlightY = slotY;
                    }
                }
            }
            slotNumber++;
        }
        recipeLayout.getItemStacks().addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
            if (!input) {
                MergedLootTableEntry entry = recipe.getOutputs().get(slotIndex - INPUT_SLOTS);
                JeiUtils.addLootTableEntryTooltips(entry, tooltip);
            } else {
                if (slotIndex == 0 && recipe.isWaterlogged()) {
                    tooltip.add(Messages.lang("tooltip.jei.waterlogged"));
                }
            }
        });
    }

    @Override
    public void setIngredients(JeiHeavySieveRecipe heavySieveRecipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, heavySieveRecipe.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, heavySieveRecipe.getOutputItems());
    }*/
}
