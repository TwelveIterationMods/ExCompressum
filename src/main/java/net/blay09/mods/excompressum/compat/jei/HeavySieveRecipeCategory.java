package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.gui.Focus;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveReward;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

public class HeavySieveRecipeCategory extends BlankRecipeCategory<HeavySieveRecipe> {

	public static final String UID = "excompressum:heavySieve";
	private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_heavy_sieve.png");

	private final IDrawableStatic background;
	private final IDrawableStatic slotHighlight;
	private boolean hasHighlight;
	private int highlightX;
	private int highlightY;

	public HeavySieveRecipeCategory(IGuiHelper guiHelper) {
		this.background = guiHelper.createDrawable(texture, 0, 0, 166, 130);
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
	public void setRecipe(IRecipeLayout recipeLayout, final HeavySieveRecipe recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 61, 9);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
		recipeLayout.getItemStacks().init(1, true, 87, 9);
		recipeLayout.getItemStacks().set(1, ingredients.getInputs(ItemStack.class).get(1));

		IFocus<?> focus = recipeLayout.getFocus();
		hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;
		final List<ItemStack> outputs = ingredients.getOutputs(ItemStack.class);
		final int INPUT_SLOTS = 2;
		int slotNumber = 0;
		for (ItemStack output : outputs) {
			final int slotX = 2 + (slotNumber % 9 * 18);
			final int slotY = 36 + (slotNumber / 9 * 18);
			recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, false, slotX, slotY);
			recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, output);
			Object focusValue = focus.getValue();
			if(focus.getMode() == Focus.Mode.OUTPUT && focusValue instanceof ItemStack) {
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
					Multiset<String> condensedTooltips = HashMultiset.create();
					for(HeavySieveReward reward : recipeWrapper.getRewardsForItemStack(ingredient)) {
						String s;
						int iChance = (int) (reward.getBaseChance() * 100f);
						if(iChance > 0) {
							s = String.format("%3d%%", (int) (reward.getBaseChance() * 100f));
						} else {
							s = String.format("%1.1f%%", reward.getBaseChance() * 100f);
						}
						if(reward.getLuckMultiplier() > 0f) {
							s += TextFormatting.BLUE + String.format(" (+ %1.1f " + I18n.format("jei.excompressum:compressedHammer.luck") + ")", reward.getLuckMultiplier());
						}
						condensedTooltips.add(s);
					}
					tooltip.add(I18n.format("jei.excompressum:heavySieve.dropChance"));
					for(String line : condensedTooltips.elementSet()) {
						tooltip.add(" * " + condensedTooltips.count(line) + "x " + line);
					}
				}
			}
		});
	}
}
