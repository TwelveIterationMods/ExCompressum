package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.compat.SieveModelBounds;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.Collection;
import java.util.Locale;
import java.util.Random;

public interface ExNihiloProvider {

	enum NihiloMod implements IStringSerializable {
		NONE,
		OMNIA,
		ADSCENSIO;

		public String getName() {
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
	boolean isHammerable(IBlockState state);
	Collection<ItemStack> rollHammerRewards(IBlockState state, int miningLevel, float luck, Random rand);
	boolean isSiftable(IBlockState state);
	boolean isSiftableWithMesh(IBlockState state, SieveMeshRegistryEntry sieveMesh);
	Collection<ItemStack> rollSieveRewards(IBlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand);
	Collection<ItemStack> rollCrookRewards(EntityLivingBase player, IBlockState state, float luck, Random rand);
	SieveModelBounds getSieveBounds();
	Collection<HeavySieveReward> generateHeavyRewards(ItemStack sourceStack, int count);
	boolean doMeshesHaveDurability();
	boolean doMeshesSplitLootTables();
	NihiloMod getNihiloMod();
	int getMeshFortune(ItemStack meshStack);
	int getMeshEfficiency(ItemStack meshStack);

}
