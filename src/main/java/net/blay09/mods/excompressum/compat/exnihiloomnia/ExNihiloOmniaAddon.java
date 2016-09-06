package net.blay09.mods.excompressum.compat.exnihiloomnia;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.registries.hammering.HammerRegistryEntry;
import exnihiloomnia.registries.hammering.HammerReward;
import exnihiloomnia.registries.sifting.SieveRegistry;
import exnihiloomnia.registries.sifting.SieveRegistryEntry;
import exnihiloomnia.registries.sifting.SieveReward;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class ExNihiloOmniaAddon implements ExNihiloProvider {

	private final EnumMap<NihiloItems, Item> itemMap = Maps.newEnumMap(NihiloItems.class);
	private final EnumMap<NihiloBlocks, Block> blockMap = Maps.newEnumMap(NihiloBlocks.class);

	private static final float SIEVE_LUCK_MODIFIER = 2.5f;

	public ExNihiloOmniaAddon() {
		itemMap.put(NihiloItems.HammerDiamond, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "hammer_diamond")));

		blockMap.put(NihiloBlocks.Dust, Block.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "dust")));

		ExRegistro.instance = this;
	}

	@Nullable
	@Override
	public Item getNihiloItem(NihiloItems type) {
		return itemMap.get(type);
	}

	@Nullable
	@Override
	public Block getNihiloBlock(NihiloBlocks type) {
		return blockMap.get(type);
	}

	@Override
	public boolean isHammerable(IBlockState state) {
		return HammerRegistry.isHammerable(state);
	}

	@Override
	public Collection<ItemStack> rollHammerRewards(IBlockState state, float luck, Random rand) {
		HammerRegistryEntry entry = HammerRegistry.getEntryForBlockState(state);
		if(entry != null) {
			List<ItemStack> list = Lists.newArrayList();
			for(HammerReward reward : entry.getRewards()) {
				int fortuneModifier = reward.getFortuneModifier();
				int chance = reward.getBaseChance() + (int) (fortuneModifier * luck); // TODO Omnia's fortune modifier is weird, looks like a bug (it does .getFortuneModifier() * .getFortuneModifier() rather than taking the hammer fortune level into account; fixed here, go report
				if(rand.nextInt(100) < chance) {
					list.add(reward.getItem().copy());
				}
			}
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public boolean isSiftable(IBlockState state) {
		return SieveRegistry.isSiftable(state);
	}

	@Override
	public Collection<ItemStack> rollSieveRewards(IBlockState state, float luck, Random rand) {
		List<ItemStack> list = Lists.newArrayList();
		SieveRegistryEntry genericEntry = SieveRegistry.getEntryForBlockState(state, EnumMetadataBehavior.IGNORED);
		if(genericEntry != null) {
			rollSieveRewardsToList(genericEntry, list, luck, rand);
		}
		SieveRegistryEntry entry = SieveRegistry.getEntryForBlockState(state, EnumMetadataBehavior.SPECIFIC);
		if(entry != null) {
			rollSieveRewardsToList(entry, list, luck, rand);
		}
		return list;
	}

	private void rollSieveRewardsToList(SieveRegistryEntry entry, List<ItemStack> list, float luck, Random rand) {
		for(SieveReward reward : entry.getRewards()) {
			if(rand.nextInt(100) < reward.getBaseChance() + SIEVE_LUCK_MODIFIER * luck) { // TODO Sieve Rewards in Omnia have no luck modifier at the moment; I randomly picked 2.5f for now. Balance it, maybe config it too.
				list.add(reward.getItem().copy());
			}
		}
	}
}
