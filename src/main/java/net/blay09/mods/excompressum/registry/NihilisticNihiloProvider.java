package net.blay09.mods.excompressum.registry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class NihilisticNihiloProvider implements ExNihiloProvider {
	@Nullable
	@Override
	public Item getNihiloItem(NihiloItems type) {
		return null;
	}

	@Nullable
	@Override
	public Block getNihiloBlock(NihiloBlocks type) {
		return null;
	}

	@Override
	public boolean isHammerable(IBlockState state) {
		return false;
	}

	@Override
	public Collection<ItemStack> rollHammerRewards(IBlockState state, float luck, Random rand) {
		return Collections.emptyList();
	}

	@Override
	public boolean isSiftable(IBlockState state) {
		return false;
	}

	@Override
	public Collection<ItemStack> rollSieveRewards(IBlockState state, float luck, Random rand) {
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public ItemStack rollSilkWorm(EntityLivingBase player, IBlockState state, int fortune) {
		return null;
	}
}
