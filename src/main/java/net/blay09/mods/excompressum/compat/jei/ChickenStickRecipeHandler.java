package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class ChickenStickRecipeHandler implements IRecipeHandler<ChickenStickRecipe> {
	@Nonnull
	@Override
	public Class<ChickenStickRecipe> getRecipeClass() {
		return ChickenStickRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return ChickenStickRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull ChickenStickRecipe recipe) {
		return ChickenStickRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull ChickenStickRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull ChickenStickRecipe recipe) {
		return true;
	}
}
