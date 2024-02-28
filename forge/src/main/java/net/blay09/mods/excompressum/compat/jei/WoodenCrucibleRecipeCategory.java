package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
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
    private final IDrawable slotHighlight;
    private final IDrawable icon;
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public WoodenCrucibleRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
        this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
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
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, JeiWoodenCrucibleRecipe jeiWoodenCrucibleRecipe, IFocusGroup iFocusGroup) {
        // TODO
    }

    /*@Override
    public void draw(JeiWoodenCrucibleRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        if (hasHighlight) {
            slotHighlight.draw(poseStack, highlightX, highlightY);
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, final JeiWoodenCrucibleRecipe recipe, final IIngredients ingredients) {
        recipeLayout.getFluidStacks().init(0, false, 75, 10);
        recipeLayout.getFluidStacks().set(0, recipe.getFluidStack());

        IFocus<ItemStack> focus = recipeLayout.getFocus(VanillaTypes.ITEM);
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
                ItemStack focusStack = focus.getValue();
                if (hasFocus) {
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
                WoodenCrucibleRecipe entry = recipe.getEntryAt(slotIndex - INPUT_SLOTS);
                ResourceLocation registryName = Objects.requireNonNull(recipe.getFluid().defaultFluidState().createLegacyBlock().getBlock().getRegistryName());
                TranslatableComponent fluidComponent = new TranslatableComponent("block." + registryName.getNamespace() + "." + registryName.getPath());
                TextComponent amountComponent = new TextComponent(String.valueOf(entry.getAmount()));
                TranslatableComponent tooltipComponent = Messages.lang("tooltip.jei.wooden_crucible.fluid_amount", amountComponent, fluidComponent);
                tooltip.add(tooltipComponent);
            }
        });
    }

    @Override
    public void setIngredients(JeiWoodenCrucibleRecipe woodenCrucibleRecipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, woodenCrucibleRecipe.getInputs());
        ingredients.setOutput(VanillaTypes.FLUID, woodenCrucibleRecipe.getFluidStack());
    }*/
}
