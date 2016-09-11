package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.compat.SieveModelBounds;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class NihilisticNihiloProvider implements ExNihiloProvider {
	private final SieveModelBounds nullBounds = new SieveModelBounds(0f, 0f, 0f, 0f);

	@Nullable
	@Override
	public ItemStack getNihiloItem(NihiloItems type) {
		return null;
	}

	@Override
	public boolean isHammerable(IBlockState state) {
		return false;
	}

	@Override
	public Collection<ItemStack> rollHammerRewards(IBlockState state, int miningLevel, float luck, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isSiftable(IBlockState state) {
		return false;
	}

	@Override
	public boolean isSiftableWithMesh(IBlockState state, SieveMeshRegistryEntry sieveMesh) {
		return false;
	}

	@Override
	public Collection<ItemStack> rollSieveRewards(IBlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public Collection<ItemStack> rollCrookRewards(EntityLivingBase player, IBlockState state, float luck, Random rand) {
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
}
