package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.compat.SieveModelBounds;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveReward;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public interface ExNihiloProvider {

	enum NihiloMod {
		Nihilistic,
		Omnia,
		Adscensio
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

	@Nullable
	ItemStack getNihiloItem(NihiloItems type);
	boolean isHammerable(IBlockState state);
	Collection<ItemStack> rollHammerRewards(IBlockState state, int miningLevel, float luck, Random rand);
	boolean isSiftable(IBlockState state);
	Collection<ItemStack> rollSieveRewards(IBlockState state, int meshLevel, float luck, Random rand);
	@Nullable
	ItemStack rollSilkWorm(EntityLivingBase player, IBlockState state, int fortune); // TODO Adscensio has a CrookRegistry, so we need something for that too; just turn this into "doTheCrookThingies"
	SieveModelBounds getSieveBounds();
	Collection<HeavySieveReward> generateHeavyRewards(ItemStack sourceStack, int count);
	boolean doMeshesHaveDurability();
	NihiloMod getNihiloMod();

}
