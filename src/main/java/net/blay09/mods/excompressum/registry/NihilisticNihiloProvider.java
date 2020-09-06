package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.SieveModelBounds;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class NihilisticNihiloProvider implements ExNihiloProvider {
	private final SieveModelBounds nullBounds = new SieveModelBounds(0f, 0f, 0f, 0f);

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
	public Collection<ItemStack> rollCrookRewards(EntityLivingBase player, BlockState state, float luck, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public SieveModelBounds getSieveBounds() {
		return nullBounds;
	}

	@Override
	public Collection<HeavySieveReward> generateHeavyRewards(ItemStack sourceStack, int count) {
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
	public NihiloMod getNihiloMod() {
		return NihiloMod.NONE;
	}

	@Override
	public int getMeshFortune(ItemStack meshStack) {
		return 0;
	}

	@Override
	public int getMeshEfficiency(ItemStack meshStack) {
		return 0;
	}
}
