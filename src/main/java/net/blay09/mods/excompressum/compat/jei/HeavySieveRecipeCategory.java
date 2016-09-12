package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import mezz.jei.JeiHelpers;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveReward;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class HeavySieveRecipeCategory implements IRecipeCategory<HeavySieveRecipe> {

	private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_heavy_sieve.png");

	public static final String UID = "excompressum:heavySieve";
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
		return I18n.format("jei.excompressum:heavySieve");
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
	public void drawAnimations(@Nonnull Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull final HeavySieveRecipe recipeWrapper) {
		recipeLayout.getItemStacks().init(0, true, 61, 9);
		Object inputMesh = recipeWrapper.getInputs().get(0);
		recipeLayout.getItemStacks().setFromRecipe(0, inputMesh);
		recipeLayout.getItemStacks().init(1, true, 87, 9);
		recipeLayout.getItemStacks().set(1, (ItemStack) recipeWrapper.getInputs().get(1));

		IFocus<ItemStack> focus = recipeLayout.getItemStacks().getFocus();
		hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;
		final List outputs = recipeWrapper.getOutputs();
		final int INPUT_SLOTS = 2;
		int slotNumber = 0;
		for(int i = 0; i < outputs.size(); i++) {
			final int slotX = 2 + (slotNumber % 9 * 18);
			final int slotY = 36 + (slotNumber / 9 * 18);
			recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, false, slotX, slotY);
			Object output = outputs.get(i);
			if(output instanceof Collection) {
				//noinspection unchecked /// Thanks Java, you're the best <3
				recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, (Collection<ItemStack>) output);
			} else if (output instanceof ItemStack) {
				ItemStack outputItemStack = (ItemStack) output;
				recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, outputItemStack);
				ItemStack focusStack = focus.getValue();
				if(focus.getMode() == IFocus.Mode.OUTPUT && focusStack != null
						&& focusStack.getItem() == outputItemStack.getItem()
						&& focusStack.getItemDamage() == outputItemStack.getItemDamage()) { // TODO broken item comparison probably
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
						String s = String.format("%3d%%", (int) (reward.getBaseChance() * 100f));
						if(reward.getLuckMultiplier() > 0f) {
							s += TextFormatting.BLUE + String.format(" (+ %1.1f luck)", reward.getLuckMultiplier()); // i18n
						}
						condensedTooltips.add(s);
					}
					// TODO figure out the sorting of these
					tooltip.add("Drop Chance:"); // i18n
					for(String line : condensedTooltips.elementSet()) {
						tooltip.add(" * " + condensedTooltips.count(line) + "x " + line);
					}
				}
			}
		});
	}
}
