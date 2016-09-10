package net.blay09.mods.excompressum.registry.compressedhammer;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;

import java.util.List;

public class CompressedHammerRegistryEntry {
	private final IBlockState inputState;
	private final boolean isWildcard;
	private final List<CompressedHammerReward> rewards = Lists.newArrayList();

	public CompressedHammerRegistryEntry(IBlockState inputState, boolean isWildcard) {
		this.inputState = inputState;
		this.isWildcard = isWildcard;
	}

	public IBlockState getInputState() {
		return inputState;
	}

	public boolean isWildcard() {
		return isWildcard;
	}

	public List<CompressedHammerReward> getRewards() {
		return rewards;
	}

	public void addReward(CompressedHammerReward reward) {
		rewards.add(reward);
	}
}
