package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistryEntry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.item.ItemStack;

import java.util.List;

public class CompressedHammerRecipe extends BlankRecipeWrapper {

	private final ItemStack input;
	private final List<ItemStack> outputs;
	private final List<CompressedHammerReward> rewards;

	public CompressedHammerRecipe(CompressedHammerRegistryEntry entry) {
		input = StupidUtils.getItemStackFromState(entry.getInputState());
		outputs = Lists.newArrayList();
		rewards = entry.getRewards();
		for(CompressedHammerReward reward : rewards) {
			outputs.add(reward.getItemStack());
		}
	}

	public CompressedHammerReward getRewardAt(int index) {
		return rewards.get(index);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(ItemStack.class, input);
		ingredients.setOutputs(ItemStack.class, outputs);
	}

}
