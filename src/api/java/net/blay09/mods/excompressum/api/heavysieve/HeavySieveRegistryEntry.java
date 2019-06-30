package net.blay09.mods.excompressum.api.heavysieve;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ModConfig;
import net.minecraft.block.state.IBlockState;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
        if (!ExCompressumAPI.getExNihilo().doMeshesSplitLootTables()) {
            return rewards;
        }

        if (ModConfig.general.flattenSieveRecipes) {
            return rewards.stream().filter(it -> sieveMesh.getMeshLevel() >= it.getMeshLevel()).collect(Collectors.toList());
        }

        return meshRewards.get(sieveMesh.getMeshLevel());
    }

    public void addReward(HeavySieveReward reward) {
        rewards.add(reward);
        if (ExCompressumAPI.getExNihilo().doMeshesSplitLootTables()) {
            meshRewards.put(reward.getMeshLevel(), reward);
        }
    }

    public void addRewards(Collection<HeavySieveReward> rewards) {
        this.rewards.addAll(rewards);
        if (ExCompressumAPI.getExNihilo().doMeshesSplitLootTables()) {
            for (HeavySieveReward reward : rewards) {
                meshRewards.put(reward.getMeshLevel(), reward);
            }
        }
    }

}
