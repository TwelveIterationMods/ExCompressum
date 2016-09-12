package net.blay09.mods.excompressum.compat.jei;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

public class HeavySieveRecipeHandler implements IRecipeHandler<HeavySieveRecipe> {
	@Nonnull
	@Override
	public Class<HeavySieveRecipe> getRecipeClass() {
		return HeavySieveRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return HeavySieveRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull HeavySieveRecipe recipe) {
		return HeavySieveRecipeCategory.UID;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull HeavySieveRecipe recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull HeavySieveRecipe recipe) {
		return true;
	}
}
