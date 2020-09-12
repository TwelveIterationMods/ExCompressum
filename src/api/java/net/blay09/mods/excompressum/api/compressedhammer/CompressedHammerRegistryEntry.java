package net.blay09.mods.excompressum.api.compressedhammer;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;

import java.util.Collection;
import java.util.List;

public class CompressedHammerRegistryEntry {
	private final BlockState inputState;
	private final List<CompressedHammerReward> rewards = Lists.newArrayList();

	public CompressedHammerRegistryEntry(BlockState inputState) {
		this.inputState = inputState;
	}

	public BlockState getInputState() {
		return inputState;
	}

	public List<CompressedHammerReward> getRewards() {
		return rewards;
	}

	public void addReward(CompressedHammerReward reward) {
		rewards.add(reward);
	}

	public void addRewards(Collection<CompressedHammerReward> rewards) {
		this.rewards.addAll(rewards);
	}
}
