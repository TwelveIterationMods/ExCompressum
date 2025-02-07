package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.recipe.CompressedHammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HammerRecipe;
import net.blay09.mods.excompressum.api.recipe.HeavySieveRecipe;
import net.blay09.mods.excompressum.api.recipe.SieveRecipe;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;

import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;

import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NihilisticNihiloProvider implements ExNihiloProvider {
	public NihilisticNihiloProvider() {
		SieveMeshRegistry.registerDefaults(null);
	}

	@Override
	public boolean isHammerable(Level level, BlockState state) {
		return false;
	}

	@Override
	public List<ItemStack> rollHammerRewards(Level level, BlockState state, ItemStack itemStack, RandomSource rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isSiftableWithMesh(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh) {
		return false;
	}

	@Override
	public boolean isHeavySiftableWithMesh(BlockState sieveState, BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh) {
		return false;
	}

	@Override
	public Collection<ItemStack> rollSieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
		return Collections.emptyList();
	}

	@Override
	public Collection<ItemStack> rollHeavySieveRewards(Level level, BlockState sieveState, BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, RandomSource rand) {
		return Collections.emptyList();
	}

	@Override
	public Collection<ItemStack> rollCompressedHammerRewards(Level level, LootContext context, ItemStack itemStack) {
		return List.of();
	}

	@Override
	public List<ItemStack> rollCrookRewards(ServerLevel level, BlockPos pos, BlockState state, @Nullable Entity entity, ItemStack tool, RandomSource rand) {
		return Collections.emptyList();
	}

	@Override
	public LootTable generateHeavySieveLootTable(Level level, BlockState sieveState, ItemLike source, int count, SieveMeshRegistryEntry mesh) {
		return LootTable.EMPTY;
	}

	@Override
	public boolean doMeshesHaveDurability() {
		return true;
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
	public boolean isHammerableCompressed(ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean isHammerableOre(ItemStack itemStack) {
		return false;
	}

	@Override
	public List<HammerRecipe> getHammerRecipes() {
		return List.of();
	}

	@Override
	public List<CompressedHammerRecipe> getCompressedHammerRecipes() {
		return List.of();
	}

	@Override
	public List<SieveRecipe> getSieveRecipes() {
		return List.of();
	}

	@Override
	public List<HeavySieveRecipe> getHeavySieveRecipes() {
		return List.of();
	}
}
