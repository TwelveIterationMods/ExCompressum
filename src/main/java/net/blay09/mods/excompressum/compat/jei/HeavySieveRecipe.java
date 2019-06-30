package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.registry.RegistryKey;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public class HeavySieveRecipe implements IRecipeWrapper {

	private final HeavySieveRegistryEntry entry;
	private final List<Object> inputs;
	private final List<ItemStack> outputs;
	private final ArrayListMultimap<RegistryKey, HeavySieveReward> rewards;

	public HeavySieveRecipe(HeavySieveRegistryEntry entry) {
		this(entry, null);
	}

	public HeavySieveRecipe(HeavySieveRegistryEntry entry, @Nullable SieveMeshRegistryEntry sieveMesh) {
		this.entry = entry;
		inputs = Lists.newArrayList();
		rewards = ArrayListMultimap.create();
		if(sieveMesh != null) {
			inputs.add(sieveMesh.getItemStack());
			for(HeavySieveReward reward : entry.getRewardsForMesh(sieveMesh, ModConfig.general.flattenSieveRecipes)) {
				rewards.put(new RegistryKey(reward.getItemStack()), reward);
			}
		} else {
			List<ItemStack> sieveMeshes = Lists.newArrayList();
			for(SieveMeshRegistryEntry meshEntry : SieveMeshRegistry.getEntries().values()) {
				sieveMeshes.add(meshEntry.getItemStack());
			}
			inputs.add(sieveMeshes);
			for(HeavySieveReward reward : entry.getRewards()) {
				rewards.put(new RegistryKey(reward.getItemStack()), reward);
			}
		}
		outputs = Lists.newArrayList();
		for(RegistryKey key : rewards.keySet()) {
			outputs.add(rewards.get(key).get(0).getItemStack());
		}
		ItemStack inputStack = StupidUtils.getItemStackFromState(entry.getInputState());
		inputs.add(inputStack);
	}

	public Collection<HeavySieveReward> getRewardsForItemStack(ItemStack itemStack) {
		return rewards.get(new RegistryKey(itemStack));
	}

	public HeavySieveRegistryEntry getEntry() {
		return entry;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		List<List<ItemStack>> inputs = JEIAddon.jeiHelpers.getStackHelper().expandRecipeItemStackInputs(this.inputs);
		ingredients.setInputLists(ItemStack.class, inputs);
		ingredients.setOutputs(ItemStack.class, outputs);
	}

}
