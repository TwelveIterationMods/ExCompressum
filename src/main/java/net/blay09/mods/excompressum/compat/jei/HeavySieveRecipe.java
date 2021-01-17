package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveRegistryEntry;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.api.sievemesh.MeshType;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySiftable;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class HeavySieveRecipe {

    private final HeavySiftable entry;
    private final List<List<ItemStack>> inputs;
    private final List<LootTableEntry> outputs;
    private final List<ItemStack> outputItems;
    private final ArrayListMultimap<ResourceLocation, HeavySieveReward> rewards;
    private final boolean waterlogged;

    public HeavySieveRecipe(HeavySiftable entry) {
        this.entry = entry;
        inputs = new ArrayList<>();
        rewards = ArrayListMultimap.create();
        if (entry.getMinimumMesh() != null) {
            SieveMeshRegistryEntry minimumMesh = SieveMeshRegistry.getEntry(entry.getMinimumMesh());
            List<ItemStack> meshItems = new ArrayList<>();
            for (SieveMeshRegistryEntry mesh : SieveMeshRegistry.getEntries().values()) {
                if (mesh.getMeshLevel() >= minimumMesh.getMeshLevel()) {
                    meshItems.add(mesh.getItemStack());
                }
            }
            inputs.add(meshItems);
        } else if (entry.getMeshes() != null) {
            List<ItemStack> meshItems = new ArrayList<>();
            for (MeshType meshType : entry.getMeshes()) {
                for (SieveMeshRegistryEntry mesh : SieveMeshRegistry.getEntries().values()) {
                    if (mesh.getMeshType() == meshType) {
                        meshItems.add(mesh.getItemStack());
                    }
                }
            }
            inputs.add(meshItems);
        } else {
            inputs.add(Collections.emptyList());
        }
        inputs.add(Arrays.asList(entry.getSource().getMatchingStacks()));
        outputs = LootTableUtils.getLootTableEntries(entry.getLootTable());
        outputItems = outputs.stream().map(LootTableEntry::getItemStack).collect(Collectors.toList());
        waterlogged = entry.isWaterlogged();
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

    public List<LootTableEntry> getOutputs() {
        return outputs;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }

    public boolean isWaterlogged() {
        return waterlogged;
    }
}
