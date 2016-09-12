package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.blay09.mods.excompressum.registry.RegistryKey;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HeavySieveRecipe implements IRecipeWrapper {

	private final HeavySieveRegistryEntry entry;
	private final SieveMeshRegistryEntry sieveMesh;
	private final List<Object> inputs;
	private final List<ItemStack> outputs;
	private final ArrayListMultimap<RegistryKey, HeavySieveReward> rewards;

	public HeavySieveRecipe(HeavySieveRegistryEntry entry) {
		this(entry, null);
	}

	public HeavySieveRecipe(HeavySieveRegistryEntry entry, @Nullable SieveMeshRegistryEntry sieveMesh) {
		this.entry = entry;
		this.sieveMesh = sieveMesh;
		inputs = Lists.newArrayList();
		rewards = ArrayListMultimap.create();
		if(sieveMesh != null) {
			inputs.add(sieveMesh);
			for(HeavySieveReward reward : entry.getRewardsForMesh(sieveMesh)) {
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
	public List getInputs() {
		return inputs;
	}

	@Override
	public List getOutputs() {
		return outputs;
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return Collections.emptyList();
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return Collections.emptyList();
	}

	@Override
	public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

	}

	@Override
	public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

	}

	@Nullable
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return Collections.emptyList();
	}

	@Override
	public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

}
