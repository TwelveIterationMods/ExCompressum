package net.blay09.mods.excompressum.compat.exnihiloomnia;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import exnihiloomnia.blocks.ENOBlocks;
import exnihiloomnia.registries.composting.CompostRegistry;
import exnihiloomnia.registries.composting.CompostRegistryEntry;
import exnihiloomnia.registries.hammering.HammerRegistry;
import exnihiloomnia.registries.hammering.HammerRegistryEntry;
import exnihiloomnia.registries.hammering.HammerReward;
import exnihiloomnia.registries.sifting.SieveRegistry;
import exnihiloomnia.registries.sifting.SieveRegistryEntry;
import exnihiloomnia.registries.sifting.SieveReward;
import exnihiloomnia.util.Color;
import exnihiloomnia.util.enums.EnumMetadataBehavior;
import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class ExNihiloOmniaAddon implements ExNihiloProvider, IAddon {

	private final EnumMap<NihiloItems, Item> itemMap = Maps.newEnumMap(NihiloItems.class);
	private final EnumMap<NihiloBlocks, Block> blockMap = Maps.newEnumMap(NihiloBlocks.class);

	private static final float SIEVE_LUCK_MODIFIER = 2.5f;

	public ExNihiloOmniaAddon() {
		itemMap.put(NihiloItems.HAMMER_WOODEN, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "hammer_wood")));
		itemMap.put(NihiloItems.HAMMER_STONE, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "hammer_stone")));
		itemMap.put(NihiloItems.HAMMER_IRON, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "hammer_iron")));
		itemMap.put(NihiloItems.HAMMER_GOLD, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "hammer_gold")));
		itemMap.put(NihiloItems.HAMMER_DIAMOND, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "hammer_diamond")));
		itemMap.put(NihiloItems.CROOK_WOODEN, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "crook_wood")));
		itemMap.put(NihiloItems.SILK_MESH, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "mesh_silk_white")));
		itemMap.put(NihiloItems.SEEDS_GRASS, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "seeds_grass")));
		itemMap.put(NihiloItems.SILK_WORM, Item.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "silkworm")));

		blockMap.put(NihiloBlocks.DUST, Block.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "dust")));
		blockMap.put(NihiloBlocks.SIEVE, Block.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "sieve_wood")));
		blockMap.put(NihiloBlocks.NETHER_GRAVEL, Block.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "gravel_nether")));
		blockMap.put(NihiloBlocks.ENDER_GRAVEL, Block.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "gravel_ender")));
		blockMap.put(NihiloBlocks.INFESTED_LEAVES, Block.REGISTRY.getObject(new ResourceLocation(Compat.EXNIHILOOMNIA, "infested_leaves")));

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

	@Nullable
	@Override
	public ItemStack rollSilkWorm(EntityPlayer player, IBlockState state, int fortune) {
		if(player.worldObj.rand.nextInt(100) == 0 || state.getBlock() == getNihiloBlock(NihiloBlocks.INFESTED_LEAVES)) {
			Item silkWormItem = getNihiloItem(NihiloItems.SILK_WORM);
			if(silkWormItem != null) {
				return new ItemStack(silkWormItem);
			}
		}
		return null;
	}

	private void rollSieveRewardsToList(SieveRegistryEntry entry, List<ItemStack> list, float luck, Random rand) {
		for(SieveReward reward : entry.getRewards()) {
			if(rand.nextInt(100) < reward.getBaseChance() + SIEVE_LUCK_MODIFIER * luck) { // TODO Sieve Rewards in Omnia have no luck modifier at the moment; I randomly picked 2.5f for now. Balance it, maybe config it too.
				list.add(reward.getItem().copy());
			}
		}
	}

	@Override
	public void loadConfig(Configuration config) {

	}

	@Override
	public void postInit() {
		if(ExCompressumConfig.enableWoodChippings) {
			HammerRegistryEntry log = new HammerRegistryEntry(Blocks.LOG.getDefaultState(), EnumMetadataBehavior.IGNORED);
			log.addReward(new ItemStack(ModItems.woodChipping), 100, 0);
			log.addReward(new ItemStack(ModItems.woodChipping), 75, 0);
			log.addReward(new ItemStack(ModItems.woodChipping), 50, 0);
			log.addReward(new ItemStack(ModItems.woodChipping), 25, 0);
			HammerRegistry.add(log);

			HammerRegistryEntry log2 = new HammerRegistryEntry(Blocks.LOG2.getDefaultState(), EnumMetadataBehavior.IGNORED);
			log2.addReward(new ItemStack(ModItems.woodChipping), 100, 0);
			log2.addReward(new ItemStack(ModItems.woodChipping), 75, 0);
			log2.addReward(new ItemStack(ModItems.woodChipping), 50, 0);
			log2.addReward(new ItemStack(ModItems.woodChipping), 25, 0);
			HammerRegistry.add(log2);

			List<ItemStack> oreDictStacks = OreDictionary.getOres("dustWood", false);
			for(ItemStack itemStack : oreDictStacks) {
				CompostRegistry.add(new CompostRegistryEntry(itemStack, 125, new Color(0xFFC77826)));
			}
		}
	}

	@Override
	public void serverStarted(FMLServerStartedEvent event) {

	}
}
