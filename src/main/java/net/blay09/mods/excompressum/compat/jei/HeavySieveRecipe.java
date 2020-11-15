package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySiftable;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class HeavySieveRecipe {

    private final HeavySiftable entry;
    private final List<List<ItemStack>> inputs;
    private final List<ItemStack> outputs;
    private final ArrayListMultimap<ResourceLocation, HeavySieveReward> rewards;

    public HeavySieveRecipe(HeavySiftable entry) {
        this(entry, null);
    }

    public HeavySieveRecipe(HeavySiftable entry, @Nullable SieveMeshRegistryEntry sieveMesh) {
        this.entry = entry;
        inputs = new ArrayList<>();
        rewards = ArrayListMultimap.create();
        if (sieveMesh != null) {
            inputs.add(Collections.singletonList(sieveMesh.getItemStack()));
            /*for (HeavySieveReward reward : entry.getRewardsForMesh(sieveMesh, ExCompressumConfig.COMMON.flattenSieveRecipes.get())) {
                rewards.put(reward.getItemStack().getItem().getRegistryName(), reward);
            }*/
        } else {
            List<ItemStack> sieveMeshes = new ArrayList<>();
            for (SieveMeshRegistryEntry meshEntry : SieveMeshRegistry.getEntries().values()) {
                sieveMeshes.add(meshEntry.getItemStack());
            }
            inputs.add(sieveMeshes);
            /*for (HeavySieveReward reward : entry.getRewards()) {
                rewards.put(reward.getItemStack().getItem().getRegistryName(), reward);
            }*/
        }
        outputs = Lists.newArrayList();
        for (ResourceLocation key : rewards.keySet()) {
            outputs.add(rewards.get(key).get(0).getItemStack());
        }
        //ItemStack inputStack = StupidUtils.getItemStackFromState(entry.getInputState());
        //inputs.add(Collections.singletonList(inputStack));
    }

    public Collection<HeavySieveReward> getRewardsForItemStack(ItemStack itemStack) {
        return rewards.get(itemStack.getItem().getRegistryName());
    }

    public HeavySiftable getEntry() {
        return entry;
    }

    public List<List<ItemStack>> getInputs() {
        return inputs;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }
}
