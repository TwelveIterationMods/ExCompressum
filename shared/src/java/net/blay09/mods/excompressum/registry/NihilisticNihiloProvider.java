package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.IHammerRecipe;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;

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
	public List<ItemStack> rollHammerRewards(BlockState state, ItemStack itemStack, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isSiftableWithMesh(BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh) {
		return false;
	}

	@Override
	public Collection<ItemStack> rollSieveRewards(BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public List<ItemStack> rollCrookRewards(ServerLevel level, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public LootTable generateHeavySieveLootTable(BlockState sieveState, ItemLike source, int count, SieveMeshRegistryEntry mesh) {
		return LootTable.EMPTY;
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

	@Override
	public List<IHammerRecipe> getHammerRecipes() {
		return Collections.emptyList();
	}
}
