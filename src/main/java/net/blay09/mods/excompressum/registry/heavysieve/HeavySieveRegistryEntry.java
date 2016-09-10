package net.blay09.mods.excompressum.registry.heavysieve;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;

import java.util.List;

public class HeavySieveRegistryEntry {

	private final IBlockState inputState;
	private final boolean isWildcard;
	private final List<HeavySieveReward> rewards = Lists.newArrayList();

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

	public void addReward(HeavySieveReward reward) {
		rewards.add(reward);
	}
}
