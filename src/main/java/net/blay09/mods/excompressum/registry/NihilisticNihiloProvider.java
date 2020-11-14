package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.SieveModelBounds;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NihilisticNihiloProvider implements ExNihiloProvider {
	@Override
	public ItemStack getNihiloItem(NihiloItems type) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isHammerable(BlockState state) {
		return false;
	}

	@Override
	public Collection<ItemStack> rollHammerRewards(BlockState state, int miningLevel, float luck, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isSiftable(BlockState state) {
		return false;
	}

	@Override
	public boolean isSiftableWithMesh(BlockState state, SieveMeshRegistryEntry sieveMesh) {
		return false;
	}

	@Override
	public Collection<ItemStack> rollSieveRewards(BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public List<ItemStack> rollCrookRewards(ServerWorld world, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public Collection<HeavySieveReward> generateHeavySieveRewards(ItemStack sourceStack, int count) {
		return Collections.emptyList();
	}

	@Override
	public boolean doMeshesHaveDurability() {
		return true;
	}

	@Override
	public boolean doMeshesSplitLootTables() {
		return false;
	}

	@Override
	public int getMeshFortune(ItemStack meshStack) {
		return 0;
	}

	@Override
	public int getMeshEfficiency(ItemStack meshStack) {
		return 0;
	}

	@Override
	public boolean isCompressableOre(ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean isHammerableOre(ItemStack itemStack) {
		return false;
	}
}
