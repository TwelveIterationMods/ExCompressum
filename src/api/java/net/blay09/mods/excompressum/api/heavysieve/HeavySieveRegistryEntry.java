package net.blay09.mods.excompressum.api.heavysieve;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.ExCompressumAPI;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.block.BlockState;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class HeavySieveRegistryEntry {

    private final BlockState inputState;
    private final List<HeavySieveReward> rewards = Lists.newArrayList();
    private final ArrayListMultimap<Integer, HeavySieveReward> meshRewards = ArrayListMultimap.create();

    public HeavySieveRegistryEntry(BlockState input) {
        this.inputState = input;
    }

    public BlockState getInputState() {
        return inputState;
    }

    public List<HeavySieveReward> getRewards() {
        return rewards;
    }

    /**
     * @deprecated Use {{@link #getRewardsForMesh(SieveMeshRegistryEntry, boolean)}} with boolean parameter.
     */
    @Deprecated
    public List<HeavySieveReward> getRewardsForMesh(SieveMeshRegistryEntry sieveMesh) {
        return getRewardsForMesh(sieveMesh, false);
    }

    public List<HeavySieveReward> getRewardsForMesh(SieveMeshRegistryEntry sieveMesh, boolean flattenSieveRecipes) {
        if (!ExCompressumAPI.getExNihilo().doMeshesSplitLootTables()) {
            return rewards;
        }

        if (flattenSieveRecipes) {
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
