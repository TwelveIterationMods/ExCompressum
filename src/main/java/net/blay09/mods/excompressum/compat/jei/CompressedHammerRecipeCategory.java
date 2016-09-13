package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerReward;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;

public class CompressedHammerRecipeCategory implements IRecipeCategory<CompressedHammerRecipe> {

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
	public void drawAnimations(@Nonnull Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull final CompressedHammerRecipe recipeWrapper) {
		recipeLayout.getItemStacks().init(0, true, 74, 9);
		recipeLayout.getItemStacks().setFromRecipe(0, recipeWrapper.getInputs().get(0));

		IFocus<ItemStack> focus = recipeLayout.getItemStacks().getFocus();
		hasHighlight = focus.getMode() == IFocus.Mode.OUTPUT;

		final List outputs = recipeWrapper.getOutputs();
		final int INPUT_SLOTS = 1;
		int slotNumber = 0;
		for(int i = 0; i < outputs.size(); i++) {
			final int slotX = 2 + slotNumber * 18;
			final int slotY = 36;
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
						&& focusStack.getItemDamage() == outputItemStack.getItemDamage()) {
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
