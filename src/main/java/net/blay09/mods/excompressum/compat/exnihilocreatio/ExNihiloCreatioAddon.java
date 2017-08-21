package net.blay09.mods.excompressum.compat.exnihilocreatio;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import exnihilocreatio.blocks.BlockSieve;
import exnihilocreatio.registries.CompostRegistry;
import exnihilocreatio.registries.CrookRegistry;
import exnihilocreatio.registries.HammerRegistry;
import exnihilocreatio.registries.RegistryReloadedEvent;
import exnihilocreatio.registries.SieveRegistry;
import exnihilocreatio.registries.types.CrookReward;
import exnihilocreatio.registries.types.Siftable;
import exnihilocreatio.texturing.Color;
import exnihilocreatio.util.BlockInfo;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.api.SieveModelBounds;
import net.blay09.mods.excompressum.config.ModConfig;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.api.heavysieve.HeavySieveReward;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

import static net.minecraft.block.Block.getBlockFromItem;

public class ExNihiloCreatioAddon implements ExNihiloProvider, IAddon {

	private final EnumMap<NihiloItems, ItemStack> itemMap = Maps.newEnumMap(NihiloItems.class);

	private final SieveModelBounds bounds;
	private Enchantment sieveEfficiency;
	private Enchantment sieveFortune;

	public ExNihiloCreatioAddon() {
		MinecraftForge.EVENT_BUS.register(this);

		bounds = new SieveModelBounds(0.8125f, 0.0625f, 0.88f, 0.15625f);

		ExRegistro.instance = this;
	}

	@Override
	public void registriesComplete() {
		itemMap.put(NihiloItems.HAMMER_WOODEN, findItem("hammer_wood", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.HAMMER_STONE, findItem("hammer_stone", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.HAMMER_IRON, findItem("hammer_iron", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.HAMMER_GOLD, findItem("hammer_gold", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.HAMMER_DIAMOND, findItem("hammer_diamond", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.CROOK_WOODEN, findItem("crook_wood", 0));
		itemMap.put(NihiloItems.SILK_MESH, findItem("item_mesh", 1));
		itemMap.put(NihiloItems.IRON_MESH, findItem("item_mesh", 3));

		itemMap.put(NihiloItems.DUST, findBlock("block_dust", 0));
		itemMap.put(NihiloItems.SIEVE, findBlock("block_sieve", 0));
		itemMap.put(NihiloItems.INFESTED_LEAVES, findBlock("block_infested_leaves", 0));
		itemMap.put(NihiloItems.NETHER_GRAVEL, findBlock("block_netherrack_crushed", 0));
		itemMap.put(NihiloItems.ENDER_GRAVEL, findBlock("block_endstone_crushed", 0));
	}

	@Override
	public void init() {
		sieveEfficiency = Enchantment.getEnchantmentByLocation(Compat.EXNIHILO_CREATIO + ":sieveefficiency");
		sieveFortune = Enchantment.getEnchantmentByLocation(Compat.EXNIHILO_CREATIO + ":sievefortune");

		ItemStack stringMeshItem = getNihiloItem(NihiloItems.SILK_MESH);
		if(!stringMeshItem.isEmpty()) {
			SieveMeshRegistryEntry stringMesh = new SieveMeshRegistryEntry(stringMeshItem);
			stringMesh.setMeshLevel(1);
			stringMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/string_mesh"));
			SieveMeshRegistry.add(stringMesh);
		}

		ItemStack flintMeshItem = findItem("item_mesh", 2);
		if(!flintMeshItem.isEmpty()) {
			SieveMeshRegistryEntry flintMesh = new SieveMeshRegistryEntry(flintMeshItem);
			flintMesh.setMeshLevel(2);
			flintMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/flint_mesh"));
			SieveMeshRegistry.add(flintMesh);
		}

		ItemStack ironMeshItem = getNihiloItem(NihiloItems.IRON_MESH);
		if(!ironMeshItem.isEmpty()) {
			SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(ironMeshItem);
			ironMesh.setMeshLevel(3);
			ironMesh.setHeavy(true);
			ironMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
			SieveMeshRegistry.add(ironMesh);
		}

		ItemStack diamondMeshItem = findItem("item_mesh", 4);
		if(!diamondMeshItem.isEmpty()) {
			SieveMeshRegistryEntry diamondMesh = new SieveMeshRegistryEntry(diamondMeshItem);
			diamondMesh.setMeshLevel(4);
			diamondMesh.setHeavy(true);
			diamondMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/diamond_mesh"));
			SieveMeshRegistry.add(diamondMesh);
		}
	}

	@SubscribeEvent
	public void onRegistryReload(RegistryReloadedEvent event) {
		if(ModConfig.general.enableWoodChippings) {
			HammerRegistry.register(Blocks.LOG.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 1f, 0f, true);
			HammerRegistry.register(Blocks.LOG.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.75f, 0f, true);
			HammerRegistry.register(Blocks.LOG.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.5f, 0f, true);
			HammerRegistry.register(Blocks.LOG.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.25f, 0f, true);

			HammerRegistry.register(Blocks.LOG2.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 1f, 0f, true);
			HammerRegistry.register(Blocks.LOG2.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.75f, 0f, true);
			HammerRegistry.register(Blocks.LOG2.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.5f, 0f, true);
			HammerRegistry.register(Blocks.LOG2.getDefaultState(), new ItemStack(ModItems.woodChipping), 0, 0.25f, 0f, true);

			List<ItemStack> oreDictStacks = OreDictionary.getOres("dustWood", false);
			for (ItemStack itemStack : oreDictStacks) {
				CompostRegistry.register(itemStack.getItem(), itemStack.getItemDamage(), 0.125f, Blocks.DIRT.getDefaultState(), new Color(0xFFC77826));
			}
		}
	}

	@Override
	public SieveModelBounds getSieveBounds() {
		return bounds;
	}

	@Override
	public Collection<HeavySieveReward> generateHeavyRewards(ItemStack sourceStack, int count) {
		List<Siftable> siftables = SieveRegistry.getDrops(sourceStack);
		if(siftables != null) {
			List<HeavySieveReward> rewards = Lists.newArrayList();
			for (Siftable siftable : siftables) {
				if(siftable.getDrop().getItem() == null) {
//					ExCompressum.logger.error("Tried to generate Heavy Sieve rewards from a null reward entry: {}", sourceStack.getItem().getRegistryName());
					continue;
				}
				for (int i = 0; i < count; i++) {
					rewards.add(new HeavySieveReward(siftable.getDrop().getItemStack(), siftable.getChance(), siftable.getMeshLevel()));
				}
			}
			return rewards;
		}
		return Collections.emptyList();
	}

	private ItemStack findItem(String name, int withMetadata) {
		ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_CREATIO, name);
		Item item = Item.REGISTRY.getObject(location);
		if(item != null) {
			return new ItemStack(item, 1, withMetadata);
		}
		return ItemStack.EMPTY;
	}

	private ItemStack findBlock(String name, int withMetadata) {
		ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_CREATIO, name);
		if(Block.REGISTRY.containsKey(location)) {
			return new ItemStack(Block.REGISTRY.getObject(location), 1, withMetadata);
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack getNihiloItem(NihiloItems type) {
		ItemStack itemStack = itemMap.get(type);
		return itemStack != null ? itemStack : ItemStack.EMPTY;
	}

	@Override
	public boolean isHammerable(IBlockState state) {
		return HammerRegistry.registered(state.getBlock());
	}

	@Override
	public Collection<ItemStack> rollHammerRewards(IBlockState state, int miningLevel, float luck, Random rand) {
		return HammerRegistry.getRewardDrops(rand, state, miningLevel, (int) luck);
	}

	@Override
	public boolean isSiftable(IBlockState state) {
		Collection<Siftable> siftables = SieveRegistry.getDrops(new BlockInfo(state));
		return siftables != null && !siftables.isEmpty();
	}

	@Override
	public boolean isSiftableWithMesh(IBlockState state, SieveMeshRegistryEntry sieveMesh) {
		List<Siftable> siftables = SieveRegistry.getDrops(new BlockInfo(state));
		if(siftables != null) {
			for(Siftable siftable : siftables) {
				if(siftable.getMeshLevel() == sieveMesh.getMeshLevel()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Collection<ItemStack> rollSieveRewards(IBlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
		List<Siftable> rewards = SieveRegistry.getDrops(new BlockInfo(state));
		if(rewards != null) {
			List<ItemStack> list = Lists.newArrayList();
			for(Siftable reward : rewards) {
				if(reward.getDrop().getItem() == null) {
					continue;
				}
				int tries = rand.nextInt((int) luck + 1) + 1;
				for(int i = 0; i < tries; i++) {
					if (sieveMesh.getMeshLevel() == reward.getMeshLevel() && rand.nextDouble() < (double) reward.getChance()) {
						list.add(reward.getDrop().getItemStack());
					}
				}
			}
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public Collection<ItemStack> rollCrookRewards(EntityLivingBase player, IBlockState state, float luck, Random rand) {
		List<CrookReward> rewards = CrookRegistry.getRewards(state);
		if(rewards != null) {
			List<ItemStack> list = Lists.newArrayList();
			for (CrookReward reward : rewards) {
				if(rand.nextFloat() <= reward.getChance() + reward.getFortuneChance() * luck) {
					list.add(reward.getStack().copy());
				}
			}
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public void postInit() {

	}

	@Override
	public boolean doMeshesHaveDurability() {
		return false;
	}

	@Override
	public boolean doMeshesSplitLootTables() {
		return true;
	}

	@Override
	public NihiloMod getNihiloMod() {
		return NihiloMod.CREATIO;
	}

	@Override
	public int getMeshFortune(ItemStack meshStack) {
		return EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, meshStack) + EnchantmentHelper.getEnchantmentLevel(sieveFortune, meshStack);
	}

	@Override
	public int getMeshEfficiency(ItemStack meshStack) {
		return EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, meshStack) + EnchantmentHelper.getEnchantmentLevel(sieveEfficiency, meshStack);
	}

	@Override
	public IBlockState getSieveRenderState() {
		ItemStack itemStack = getNihiloItem(NihiloItems.SIEVE);
		if(!itemStack.isEmpty()) {
			Block block = getBlockFromItem(itemStack.getItem());
			if(block instanceof BlockSieve) {
				// apparently Creatio thinks "default state" means "invalid state"
				return block.getDefaultState().withProperty(BlockSieve.MESH, BlockSieve.MeshType.NONE);
			}
		}
		return Blocks.AIR.getDefaultState();
	}
}
