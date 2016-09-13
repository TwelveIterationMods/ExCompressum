package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class ChickenStickRecipeCategory implements IRecipeCategory<ChickenStickRecipe> {

	public static final String UID = "excompressum:chickenStick";
	private static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/gui/jei_chicken_stick.png");

	private final IDrawableStatic background;

	public ChickenStickRecipeCategory(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(texture, 0, 0, 166, 58);
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

	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull ChickenStickRecipe recipeWrapper) {
		recipeLayout.getItemStacks().init(0, true, 16, 20);
		recipeLayout.getItemStacks().setFromRecipe(0, recipeWrapper.getInputs().get(0));
		recipeLayout.getItemStacks().init(1, false, 130, 20);
		recipeLayout.getItemStacks().setFromRecipe(1, recipeWrapper.getOutputs().get(0));
	}
}
