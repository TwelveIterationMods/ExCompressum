package net.blay09.mods.excompressum.registry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
		SILK_WORM, SILK_MESH
	}

	enum NihiloBlocks {
		DUST,
		NETHER_GRAVEL,
		ENDER_GRAVEL,
		INFESTED_LEAVES, SIEVE
	}

	@Nullable
	Item getNihiloItem(NihiloItems type);
	@Nullable
	Block getNihiloBlock(NihiloBlocks type);
	boolean isHammerable(IBlockState state);
	Collection<ItemStack> rollHammerRewards(IBlockState state, float luck, Random rand);
	boolean isSiftable(IBlockState state);
	Collection<ItemStack> rollSieveRewards(IBlockState state, float luck, Random rand);
	@Nullable
	ItemStack rollSilkWorm(EntityPlayer player, IBlockState state, int fortune);
}
