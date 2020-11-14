package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public abstract class ExNihilo {

	public static ExNihiloProvider instance;

	public static boolean isNihiloItem(ItemStack itemStack, ExNihiloProvider.NihiloItems type) {
		ItemStack nihiloStack = instance.getNihiloItem(type);
		return !nihiloStack.isEmpty() && itemStack.getItem() == nihiloStack.getItem();
	}

	public static ItemStack getNihiloItem(ExNihiloProvider.NihiloItems type) {
		return instance.getNihiloItem(type);
	}

	public static boolean isHammerable(BlockState state) {
		return instance.isHammerable(state);
	}

	public static boolean isHammerable(ItemStack itemStack) {
		BlockState state = StupidUtils.getStateFromItemStack(itemStack);
		return state != null && instance.isHammerable(state);
	}

	public static Collection<ItemStack> rollHammerRewards(ItemStack itemStack, int miningLevel, float luck, Random rand) {
		BlockState state = StupidUtils.getStateFromItemStack(itemStack);
		if(state != null) {
			return instance.rollHammerRewards(state, miningLevel, luck, rand);
		}
		return Collections.emptyList();
	}

	public static boolean isSiftableWithMesh(ItemStack itemStack, @Nullable SieveMeshRegistryEntry sieveMesh) {
		BlockState state = StupidUtils.getStateFromItemStack(itemStack);
		return state != null && instance.isSiftableWithMesh(state, sieveMesh);
	}

	public static Collection<ItemStack> rollSieveRewards(BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
		return instance.rollSieveRewards(state, sieveMesh, luck, rand);
	}

	public static Collection<ItemStack> rollSieveRewards(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
		BlockState state = StupidUtils.getStateFromItemStack(itemStack);
		if(state != null) {
			return instance.rollSieveRewards(state, sieveMesh, luck, rand);
		}
		return Collections.emptyList();
	}

	public static List<ItemStack> rollCrookRewards(ServerWorld world, BlockPos pos, BlockState state, @Nullable Entity player, ItemStack tool, Random rand) {
		return instance.rollCrookRewards(world, pos, state, player, tool, rand);
	}

	public static Collection<HeavySieveReward> generateHeavySieveRewards(ItemStack sourceStack, int count) {
		return instance.generateHeavySieveRewards(sourceStack, count);
	}

	public static boolean doMeshesHaveDurability() {
		return instance.doMeshesHaveDurability();
	}

	public static boolean doMeshesSplitLootTables() {
		return instance.doMeshesSplitLootTables();
	}

	public static int getMeshFortune(ItemStack meshStack) {
		return instance.getMeshFortune(meshStack);
	}

	public static int getMeshEfficiency(ItemStack meshStack) {
		return instance.getMeshEfficiency(meshStack);
	}

	public static boolean hasNihiloMod() {
		return !(instance instanceof NihilisticNihiloProvider);
	}

	public static ExNihiloProvider getInstance() {
		return instance;
	}
}
