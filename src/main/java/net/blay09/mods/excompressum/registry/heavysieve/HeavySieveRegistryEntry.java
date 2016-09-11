package net.blay09.mods.excompressum.registry.heavysieve;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.block.state.IBlockState;

import java.util.Collection;
import java.util.List;

public class HeavySieveRegistryEntry {

	private final IBlockState inputState;
	private final boolean isWildcard;
	private final List<HeavySieveReward> rewards = Lists.newArrayList();
	private final ArrayListMultimap<Integer, HeavySieveReward> meshRewards = ArrayListMultimap.create();

	public HeavySieveRegistryEntry(IBlockState input, boolean isWildcard) {
		this.inputState = input;
		this.isWildcard = isWildcard;
	}

	public IBlockState getInputState() {
		return inputState;
	}

	public boolean isWildcard() {
		return isWildcard;
	}

	public List<HeavySieveReward> getRewards() {
		return rewards;
	}

	public List<HeavySieveReward> getRewardsForMesh(SieveMeshRegistryEntry sieveMesh) {
		if(!ExRegistro.doMeshesSplitLootTables()) {
			return rewards;
		}
		return meshRewards.get(sieveMesh.getMeshLevel());
	}

	public void addReward(HeavySieveReward reward) {
		rewards.add(reward);
		if(ExRegistro.doMeshesSplitLootTables()){
			meshRewards.put(reward.getMeshLevel(), reward);
		}
	}

	public void addRewards(Collection<HeavySieveReward> rewards) {
		this.rewards.addAll(rewards);
		if(ExRegistro.doMeshesSplitLootTables()) {
			for (HeavySieveReward reward : rewards) {
				meshRewards.put(reward.getMeshLevel(), reward);
			}
		}
	}

}
