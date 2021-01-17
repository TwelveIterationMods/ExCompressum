package net.blay09.mods.excompressum.compat.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.woodencrucible.WoodenCrucibleRegistryEntry;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleMeltable;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class WoodenCrucibleRecipeCategory implements IRecipeCategory<WoodenCrucibleRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "wooden_crucible");
    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_wooden_crucible.png");

    private final IDrawable background;
    private final IDrawable slotHighlight;
    private final IDrawable icon;
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public WoodenCrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
        this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.woodenCrucibles[0]));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends WoodenCrucibleRecipe> getRecipeClass() {
        return WoodenCrucibleRecipe.class;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return I18n.format(UID.toString());
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
    public void draw(WoodenCrucibleRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        if (hasHighlight) {
            slotHighlight.draw(matrixStack, highlightX, highlightY);
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, final WoodenCrucibleRecipe recipe, final IIngredients ingredients) {
        recipeLayout.getFluidStacks().init(0, false, 75, 10);
        recipeLayout.getFluidStacks().set(0, recipe.getFluidStack());

        IFocus<?> focus = recipeLayout.getFocus();
        boolean hasFocus = focus != null && focus.getMode() == IFocus.Mode.INPUT;
        hasHighlight = false;
        final List<List<ItemStack>> inputs = ingredients.getInputs(VanillaTypes.ITEM);
        final int INPUT_SLOTS = 1;
        int slotNumber = 0;
        for (List<ItemStack> input : inputs) {
            final int slotX = 2 + (slotNumber % 9 * 18);
            final int slotY = 36 + (slotNumber / 9 * 18);
            recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, true, slotX, slotY);
            recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, input);
            if (focus != null) {
                Object focusValue = focus.getValue();
                if (hasFocus && focusValue instanceof ItemStack) {
                    ItemStack focusStack = (ItemStack) focus.getValue();
                    for (ItemStack inputVariant : input) {
                        if (focusStack.getItem() == inputVariant.getItem()) {
                            hasHighlight = true;
                            highlightX = slotX;
                            highlightY = slotY;
                            break;
                        }
                    }
                }
            }
            slotNumber++;
        }

        recipeLayout.getItemStacks().addTooltipCallback((slotIndex, isInput, itemStack, tooltip) -> {
            if (isInput) {
                WoodenCrucibleMeltable entry = recipe.getEntryAt(slotIndex - INPUT_SLOTS);
                ResourceLocation registryName = Objects.requireNonNull(recipe.getFluid().getDefaultState().getBlockState().getBlock().getRegistryName());
                TranslationTextComponent fluidComponent = new TranslationTextComponent("block." + registryName.getNamespace() + "." + registryName.getPath());
                StringTextComponent amountComponent = new StringTextComponent(String.valueOf(entry.getAmount()));
                TranslationTextComponent tooltipComponent = Messages.lang("tooltip.jei.wooden_crucible.fluid_amount", amountComponent, fluidComponent);
                tooltip.add(tooltipComponent);
            }
        });
    }

    @Override
    public void setIngredients(WoodenCrucibleRecipe woodenCrucibleRecipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, woodenCrucibleRecipe.getInputs());
        ingredients.setOutput(VanillaTypes.FLUID, woodenCrucibleRecipe.getFluidStack());
    }
}
