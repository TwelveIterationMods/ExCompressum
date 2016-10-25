package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IFocus;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerReward;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

public class CompressedHammerRecipeCategory extends BlankRecipeCategory<CompressedHammerRecipe> {

	private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_compressed_hammer.png");
	public static final String UID = "excompressum:compressedHammer";

	private final IDrawableStatic background;
	private final IDrawableStatic slotHighlight;
	private boolean hasHighlight;
	private int highlightX;
	private int highlightY;

	public CompressedHammerRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(texture, 0, 0, 166, 63);
		this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
	}

	@Nonnull
	@Override
	public String getUid() {
		return UID;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.format("jei." + UID);
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(@Nonnull Minecraft minecraft) {
		if(hasHighlight) {
			slotHighlight.draw(minecraft, highlightX, highlightY);
		}
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, final CompressedHammerRecipe recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 74, 9);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));

		IFocus<?> focus = recipeLayout.getFocus();
		hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;

		final List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);
		final int INPUT_SLOTS = 1;
		int slotNumber = 0;
		for (ItemStack output : outputs) {
			final int slotX = 2 + slotNumber * 18;
			final int slotY = 36;
			recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, false, slotX, slotY);
			recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, output);
			Object focusValue = focus.getValue(); // why you gotta do this to me JEI
			if (focus.getMode() == IFocus.Mode.OUTPUT && focusValue instanceof ItemStack) {
				ItemStack focusStack = (ItemStack) focusValue;
				if (focusStack.getItem() == output.getItem() && focusStack.getItemDamage() == output.getItemDamage()) {
					highlightX = slotX;
					highlightY = slotY;
				}
			}
			slotNumber++;
		}

		recipeLayout.getItemStacks().addTooltipCallback(new ITooltipCallback<ItemStack>() {
			@Override
			public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
				if(!input) {
					CompressedHammerReward reward = recipeWrapper.getRewardAt(slotIndex - INPUT_SLOTS);
					tooltip.add(I18n.format("jei.excompressum:compressedHammer.dropChance"));
					String s = String.format(" * %3d%%", (int) (reward.getBaseChance() * 100f));
					if(reward.getLuckMultiplier() > 0f) {
						s += TextFormatting.BLUE + String.format(" (+ %1.1f " + I18n.format("jei.excompressum:compressedHammer.luck") + ")", reward.getLuckMultiplier());
					}
					tooltip.add(s);
				}
			}
		});
	}
}
