package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class CompressedHammerRecipeHandler implements IRecipeHandler<CompressedHammerRecipe> {
	@Nonnull
	@Override
	public Class<CompressedHammerRecipe> getRecipeClass() {
		return CompressedHammerRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return CompressedHammerRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull CompressedHammerRecipe recipe) {
		return CompressedHammerRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull CompressedHammerRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull CompressedHammerRecipe recipe) {
		return true;
	}
}
