package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.List;

public class HeavySieveRecipeCategory implements IRecipeCategory<HeavySieveRecipe> {

	public static final ResourceLocation UID = new ResourceLocation(ExCompressum.MOD_ID, "heavy_sieve");
	private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_heavy_sieve.png");

	private final IDrawable background;
	private final IDrawable slotHighlight;
	private final IDrawable icon;
	private final IJeiHelpers jeiHelpers;
	private boolean hasHighlight;
	private int highlightX;
	private int highlightY;

	public HeavySieveRecipeCategory(IJeiHelpers jeiHelpers) {
		this.jeiHelpers = jeiHelpers;
		final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		this.background = guiHelper.createDrawable(texture, 0, 0, 166, 129);
		this.slotHighlight = guiHelper.createDrawable(texture, 166, 0, 18, 18);
		this.icon = guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.heavySieves[0]));
	}

	@Nonnull
	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends HeavySieveRecipe> getRecipeClass() {
		return HeavySieveRecipe.class;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return I18n.format("block.excompressum.heavy_sieve");
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
	public void draw(HeavySieveRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
		if(hasHighlight) {
			slotHighlight.draw(matrixStack, highlightX, highlightY);
		}
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, final HeavySieveRecipe recipeWrapper, IIngredients ingredients) {
		recipeLayout.getItemStacks().init(0, true, 61, 9);
		recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
		recipeLayout.getItemStacks().init(1, true, 87, 9);
		recipeLayout.getItemStacks().set(1, ingredients.getInputs(VanillaTypes.ITEM).get(1));

		IFocus<?> focus = recipeLayout.getFocus();
		hasHighlight = focus != null && focus.getMode() == IFocus.Mode.OUTPUT;
		final List<List<ItemStack>> outputs = ingredients.getOutputs(VanillaTypes.ITEM);
		final int INPUT_SLOTS = 2;
		int slotNumber = 0;
		for (List<ItemStack> output : outputs) {
			final int slotX = 2 + (slotNumber % 9 * 18);
			final int slotY = 36 + (slotNumber / 9 * 18);
			recipeLayout.getItemStacks().init(INPUT_SLOTS + slotNumber, false, slotX, slotY);
			recipeLayout.getItemStacks().set(INPUT_SLOTS + slotNumber, output);
			if(focus != null) {
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
					condensedTooltips.add(s);
				}
				tooltip.add(new TranslationTextComponent("excompressum.tooltip.heavySieve.dropChance"));
				for(String line : condensedTooltips.elementSet()) {
					tooltip.add(new StringTextComponent(" * " + condensedTooltips.count(line) + "x " + line));
				}
			}
		});
	}

	@Override
	public void setIngredients(HeavySieveRecipe heavySieveRecipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, heavySieveRecipe.getInputs());
		ingredients.setOutputs(VanillaTypes.ITEM, heavySieveRecipe.getOutputs());
	}
}
