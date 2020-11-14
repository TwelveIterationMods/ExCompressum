package net.blay09.mods.excompressum.api;

import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Locale;
import java.util.Random;

public interface ExNihiloProvider {

	@Deprecated
	enum NihiloMod implements IStringSerializable {
		NONE,
		OMNIA,
		ADSCENSIO,
		CREATIO,
		SEQUENTIA;

		@Override
		public String getString() {
			return name().toLowerCase(Locale.ENGLISH);
		}
	}

	enum NihiloItems {
		SEEDS_GRASS,
		CROOK_WOODEN,
		HAMMER_WOODEN,
		HAMMER_STONE,
		HAMMER_IRON,
		HAMMER_GOLD,
		HAMMER_DIAMOND,
		SILK_WORM,
		SILK_MESH,
		DUST,
		NETHER_GRAVEL,
		ENDER_GRAVEL,
		INFESTED_LEAVES,
		IRON_MESH,
		SIEVE
	}

	ItemStack getNihiloItem(NihiloItems type);
	boolean isHammerable(BlockState state);
	Collection<ItemStack> rollHammerRewards(BlockState state, int miningLevel, float luck, Random rand);
	boolean isSiftable(BlockState state);
	boolean isSiftableWithMesh(BlockState state, @Nullable SieveMeshRegistryEntry sieveMesh);
	Collection<ItemStack> rollSieveRewards(BlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand);
	Collection<ItemStack> rollCrookRewards(LivingEntity player, BlockState state, float luck, Random rand);
	Collection<HeavySieveReward> generateHeavySieveRewards(ItemStack sourceStack, int count);
	boolean doMeshesHaveDurability();
	boolean doMeshesSplitLootTables();
	int getMeshFortune(ItemStack meshStack);
	int getMeshEfficiency(ItemStack meshStack);
	boolean isCompressableOre(ItemStack itemStack);
	boolean isHammerableOre(ItemStack itemStack);

}
