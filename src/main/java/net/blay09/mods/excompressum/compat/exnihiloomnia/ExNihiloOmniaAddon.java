package net.blay09.mods.excompressum.compat.exnihiloomnia;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import net.blay09.mods.excompressum.StupidUtils;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.compat.SieveModelBounds;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
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

	private final EnumMap<NihiloItems, ItemStack> itemMap = Maps.newEnumMap(NihiloItems.class);

	private static final float SIEVE_LUCK_MODIFIER = 2.5f;

	private final SieveModelBounds sieveModelBounds;

	public ExNihiloOmniaAddon() {
		itemMap.put(NihiloItems.HAMMER_WOODEN, findItem("hammer_wood", 0));
		itemMap.put(NihiloItems.HAMMER_STONE, findItem("hammer_stone", 0));
		itemMap.put(NihiloItems.HAMMER_IRON, findItem("hammer_iron", 0));
		itemMap.put(NihiloItems.HAMMER_GOLD, findItem("hammer_gold", 0));
		itemMap.put(NihiloItems.HAMMER_DIAMOND, findItem("hammer_diamond", 0));
		itemMap.put(NihiloItems.CROOK_WOODEN, findItem("crook_wood", 0));
		itemMap.put(NihiloItems.SILK_MESH, findItem("mesh_silk_white", 0));
		itemMap.put(NihiloItems.SEEDS_GRASS, findItem("seeds_grass", 0));
		itemMap.put(NihiloItems.SILK_WORM, findItem("silkworm", 0));

		itemMap.put(NihiloItems.DUST, findBlock("dust", 0));
		itemMap.put(NihiloItems.SIEVE, findBlock("sieve_wood", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.NETHER_GRAVEL, findBlock("gravel_nether", 0));
		itemMap.put(NihiloItems.ENDER_GRAVEL, findBlock("gravel_ender", 0));
		itemMap.put(NihiloItems.INFESTED_LEAVES, findBlock("infested_leaves", 0));

		sieveModelBounds = new SieveModelBounds(0.5625f, 0.0625f, 0.88f, 0.5f);

		ItemStack woodenMeshItem = findItem("mesh_wood", OreDictionary.WILDCARD_VALUE);
		if(woodenMeshItem != null) {
			SieveMeshRegistryEntry woodenMesh = new SieveMeshRegistryEntry(woodenMeshItem);
			woodenMesh.setSpriteLocation(new ResourceLocation(Compat.EXNIHILO_OMNIA, "blocks/sieve_mesh_wood"));
			SieveMeshRegistry.add(woodenMesh);
		}

		ItemStack silkMeshItem = itemMap.get(NihiloItems.SILK_MESH);
		if(silkMeshItem != null) {
			silkMeshItem = silkMeshItem.copy();
			silkMeshItem.setItemDamage(OreDictionary.WILDCARD_VALUE);
			SieveMeshRegistryEntry silkMesh = new SieveMeshRegistryEntry(silkMeshItem);
			silkMesh.setSpriteLocation(new ResourceLocation(Compat.EXNIHILO_OMNIA, "blocks/sieve_mesh_silk_white"));
			silkMesh.setMeshLevel(1);
			SieveMeshRegistry.add(silkMesh);
		}

		ExRegistro.instance = this;
	}

	@Override
	public SieveModelBounds getSieveBounds() {
		return sieveModelBounds;
	}

	@Override
	public Collection<HeavySieveReward> generateHeavyRewards(ItemStack sourceStack, int count) {
		IBlockState state = StupidUtils.getStateFromItemStack(sourceStack);
		if(state == null) {
			return Collections.emptyList();
		}
		List<HeavySieveReward> rewards = Lists.newArrayList();
		SieveRegistryEntry genericEntry = SieveRegistry.getEntryForBlockState(state, EnumMetadataBehavior.IGNORED);
		if(genericEntry != null) {
			for (SieveReward reward : genericEntry.getRewards()) {
				for (int i = 0; i < count; i++) {
					rewards.add(new HeavySieveReward(reward.getItem(), (float) reward.getBaseChance() / 100f, SIEVE_LUCK_MODIFIER));
				}
			}
		}
		SieveRegistryEntry entry = SieveRegistry.getEntryForBlockState(state, EnumMetadataBehavior.SPECIFIC);
		if(entry != null) {
			for (SieveReward reward : entry.getRewards()) {
				for (int i = 0; i < count; i++) {
					rewards.add(new HeavySieveReward(reward.getItem(), (float) reward.getBaseChance() / 100f, SIEVE_LUCK_MODIFIER));
				}
			}
		}
		return rewards;
	}

	@Nullable
	private ItemStack findItem(String name, int withMetadata) {
		ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_OMNIA, name);
		Item item = Item.REGISTRY.getObject(location);
		if(item != null) {
			return new ItemStack(item, 1, withMetadata);
		}
		return null;
	}

	@Nullable
	private ItemStack findBlock(String name, int withMetadata) {
		ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_OMNIA, name);
		if(Block.REGISTRY.containsKey(location)) {
			return new ItemStack(Block.REGISTRY.getObject(location), 1, withMetadata);
		}
		return null;
	}

	@Nullable
	@Override
	public ItemStack getNihiloItem(NihiloItems type) {
		return itemMap.get(type);
	}

	@Override
	public boolean isHammerable(IBlockState state) {
		return HammerRegistry.isHammerable(state);
	}

	@Override
	public Collection<ItemStack> rollHammerRewards(IBlockState state, int miningLevel, float luck, Random rand) {
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
	public Collection<ItemStack> rollSieveRewards(IBlockState state, int meshLevel, float luck, Random rand) {
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
	public ItemStack rollSilkWorm(EntityLivingBase player, IBlockState state, int fortune) {
		ItemStack infestedLeaves = getNihiloItem(NihiloItems.INFESTED_LEAVES);
		if(infestedLeaves != null && (player.worldObj.rand.nextInt(100) == 0 || state.getBlock() == Block.getBlockFromItem(infestedLeaves.getItem()))) {
			ItemStack silkWormItem = getNihiloItem(NihiloItems.SILK_WORM);
			if(silkWormItem != null) {
				return silkWormItem.copy();
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
