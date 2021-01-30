package net.blay09.mods.excompressum.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public class CompressedHammerRecipeCategory implements IRecipeCategory<JeiCompressedHammerRecipe> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_hammer.png");
    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "compressed_hammer");

    private final IDrawable background;
    private final IDrawable slotHighlight;
    private final IDrawable icon;
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public CompressedHammerRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 63);
        this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.autoCompressedHammer));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends JeiCompressedHammerRecipe> getRecipeClass() {
        return JeiCompressedHammerRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format(UID.toString());
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
    public void setIngredients(JeiCompressedHammerRecipe compressedHammerRecipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, compressedHammerRecipe.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, compressedHammerRecipe.getOutputItems());
    }

    @Override
    public void draw(JeiCompressedHammerRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        if (hasHighlight) {
            slotHighlight.draw(matrixStack, highlightX, highlightY);
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, final JeiCompressedHammerRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 74, 9);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

        IFocus<?> focus = recipeLayout.getFocus();
        hasHighlight = focus != null && focus.getMode() == IFocus.Mode.OUTPUT;

        final List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
        final int INPUT_SLOTS = 1;
        int slotNumber = 0;
        for (List<ItemStack> output : outputs) {
            final int slotX = 2 + slotNumber * 18;
            final int slotY = 36;
            recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, false, slotX, slotY);
            recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, output);
            if (focus != null) {
                Object focusValue = focus.getValue();
                if (focus.getMode() == IFocus.Mode.OUTPUT && focusValue instanceof ItemStack) {
                    ItemStack focusStack = (ItemStack) focusValue;
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
            }
        });
    }
}
