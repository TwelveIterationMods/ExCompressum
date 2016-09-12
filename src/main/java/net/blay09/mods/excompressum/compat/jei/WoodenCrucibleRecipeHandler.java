package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class WoodenCrucibleRecipeHandler implements IRecipeHandler<WoodenCrucibleRecipe> {
	@Nonnull
	@Override
	public Class<WoodenCrucibleRecipe> getRecipeClass() {
		return WoodenCrucibleRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return WoodenCrucibleRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull WoodenCrucibleRecipe recipe) {
		return WoodenCrucibleRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull WoodenCrucibleRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull WoodenCrucibleRecipe recipe) {
		return true;
	}
}
