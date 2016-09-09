package net.blay09.mods.excompressum.registry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public interface ExNihiloProvider {

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
		SIEVE
	}

	@Nullable
	ItemStack getNihiloItem(NihiloItems type);
	boolean isHammerable(IBlockState state);
	Collection<ItemStack> rollHammerRewards(IBlockState state, float luck, Random rand);
	boolean isSiftable(IBlockState state);
	Collection<ItemStack> rollSieveRewards(IBlockState state, float luck, Random rand);
	@Nullable
	ItemStack rollSilkWorm(EntityLivingBase player, IBlockState state, int fortune);
}
