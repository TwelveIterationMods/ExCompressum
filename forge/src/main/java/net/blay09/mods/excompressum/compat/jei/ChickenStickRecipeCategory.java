package net.blay09.mods.excompressum.compat.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class ChickenStickRecipeCategory implements IRecipeCategory<JeiChickenStickRecipe> {

    private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_hammer.png");
    public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "chicken_stick");

    private final IDrawable background;
    private final IDrawable slotHighlight;
    private final IDrawable icon;
    private boolean hasHighlight;
    private int highlightX;
    private int highlightY;

    public ChickenStickRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(texture, 0, 0, 166, 63);
        this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModItems.chickenStick));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends JeiChickenStickRecipe> getRecipeClass() {
        return JeiChickenStickRecipe.class;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return new TranslatableComponent(UID.toString());
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
    public void setIngredients(JeiChickenStickRecipe recipe, IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, recipe.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getOutputItems());
    }

    @Override
    public void draw(JeiChickenStickRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        if (hasHighlight) {
            slotHighlight.draw(poseStack, highlightX, highlightY);
        }
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, final JeiChickenStickRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 74, 9);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));

        IFocus<ItemStack> focus = recipeLayout.getFocus(VanillaTypes.ITEM);
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
            }
        });
    }
}
