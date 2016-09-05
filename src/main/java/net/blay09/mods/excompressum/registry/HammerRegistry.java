package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.registry.data.SmashableReward;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;

public class HammerRegistry {

	public static Collection<SmashableReward> getRewards(IBlockState state) {
		return new ArrayList<>();
	}

	public static Collection<SmashableReward> getRewards(ItemStack itemStack) {
		return new ArrayList<>();
	}

	public static boolean isRegistered(IBlockState state) {
		return false;
	}

	public static boolean isRegistered(ItemStack itemStack) {
		return false;
	}

	@Deprecated // TODO move to the other thing
	public static boolean isHammerUpgrade(ItemStack itemStack) {
		return false;
	}
}
