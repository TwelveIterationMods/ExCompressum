package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.compat.SieveModelBounds;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveReward;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public abstract class ExRegistro {

	public static ExNihiloProvider instance;

	public static boolean isNihiloItem(ItemStack itemStack, ExNihiloProvider.NihiloItems type) {
		ItemStack nihiloStack = instance.getNihiloItem(type);
		return nihiloStack != null && itemStack.getItem() == nihiloStack.getItem() && (itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || nihiloStack.getItemDamage() == OreDictionary.WILDCARD_VALUE || itemStack.getItemDamage() == nihiloStack.getItemDamage());
	}

	@Nullable
	public static ItemStack getNihiloItem(ExNihiloProvider.NihiloItems type) {
		return instance.getNihiloItem(type);
	}

	public static boolean isHammerable(IBlockState state) {
		return instance.isHammerable(state);
	}

	public static boolean isHammerable(ItemStack itemStack) {
		IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
		return state != null && instance.isHammerable(state);
	}

	public static Collection<ItemStack> rollHammerRewards(IBlockState state, int miningLevel, float luck, Random rand) {
		return instance.rollHammerRewards(state, miningLevel, luck, rand);
	}

	public static Collection<ItemStack> rollHammerRewards(ItemStack itemStack, int miningLevel, float luck, Random rand) {
		IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
		if(state != null) {
			return instance.rollHammerRewards(state, miningLevel, luck, rand);
		}
		return Collections.emptyList();
	}

	public static boolean isSiftable(ItemStack itemStack, int meshLevel) {
		IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
		return state != null && instance.isSiftable(state, meshLevel);
	}

	public static Collection<ItemStack> rollSieveRewards(IBlockState state, int meshLevel, float luck, Random rand) {
		return instance.rollSieveRewards(state, meshLevel, luck, rand);
	}

	public static Collection<ItemStack> rollSieveRewards(ItemStack itemStack, int meshLevel, float luck, Random rand) {
		IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
		if(state != null) {
			return instance.rollSieveRewards(state, meshLevel, luck, rand);
		}
		return Collections.emptyList();
	}

	public static Collection<ItemStack> rollCrookRewards(EntityLivingBase player, IBlockState state, float luck, Random rand) {
		return instance.rollCrookRewards(player, state, luck, rand);
	}

	public static SieveModelBounds getSieveBounds() {
		return instance.getSieveBounds();
	}

	public static Collection<HeavySieveReward> generateHeavyRewards(ItemStack sourceStack, int count) {
		return instance.generateHeavyRewards(sourceStack, count);
	}

	public static boolean doMeshesHaveDurability() {
		return instance.doMeshesHaveDurability();
	}

	public static ExNihiloProvider.NihiloMod getNihiloMod() {
		return instance.getNihiloMod();
	}

}
