package net.blay09.mods.excompressum.compat.exnihiloadscensio;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import exnihiloadscensio.registries.CompostRegistry;
import exnihiloadscensio.registries.CrookRegistry;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.HammerReward;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.CrookReward;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.BlockInfo;
import net.blay09.mods.excompressum.ExCompressum;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class ExNihiloAdscensioAddon implements ExNihiloProvider, IAddon {

	private final EnumMap<NihiloItems, ItemStack> itemMap = Maps.newEnumMap(NihiloItems.class);

	private final SieveModelBounds bounds;
	private float sieveLuckMultiplier = 0.1f;

	public ExNihiloAdscensioAddon() {
		itemMap.put(NihiloItems.HAMMER_WOODEN, findItem("hammerWood", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.HAMMER_STONE, findItem("hammerStone", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.HAMMER_IRON, findItem("hammerIron", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.HAMMER_GOLD, findItem("hammerGold", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.HAMMER_DIAMOND, findItem("hammerDiamond", OreDictionary.WILDCARD_VALUE));
		itemMap.put(NihiloItems.CROOK_WOODEN, findItem("crookWood", 0));
		itemMap.put(NihiloItems.SILK_MESH, findItem("itemMesh", 1));
		itemMap.put(NihiloItems.IRON_MESH, findItem("itemMesh", 3));

		itemMap.put(NihiloItems.DUST, findBlock("blockDust", 0));
		itemMap.put(NihiloItems.SIEVE, findBlock("blockSieve", 0)); // NOTE Adscensio only has an Oak Sieve at the moment
		itemMap.put(NihiloItems.INFESTED_LEAVES, findBlock("blockInfestedLeaves", 0));

		bounds = new SieveModelBounds(0.8125f, 0.0625f, 0.88f, 0.15625f);

		ItemStack stringMeshItem = getNihiloItem(NihiloItems.SILK_MESH);
		if(stringMeshItem != null) {
			SieveMeshRegistryEntry stringMesh = new SieveMeshRegistryEntry(stringMeshItem);
			stringMesh.setMeshLevel(1);
			stringMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/string_mesh"));
			SieveMeshRegistry.add(stringMesh);
		}

		ItemStack flintMeshItem = findItem("itemMesh", 2);
		if(flintMeshItem != null) {
			SieveMeshRegistryEntry flintMesh = new SieveMeshRegistryEntry(flintMeshItem);
			flintMesh.setMeshLevel(2);
			flintMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/flint_mesh"));
			SieveMeshRegistry.add(flintMesh);
		}

		ItemStack ironMeshItem = getNihiloItem(NihiloItems.IRON_MESH);
		if(ironMeshItem != null) {
			SieveMeshRegistryEntry ironMesh = new SieveMeshRegistryEntry(ironMeshItem);
			ironMesh.setMeshLevel(3);
			ironMesh.setHeavy(true);
			ironMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/iron_mesh"));
			SieveMeshRegistry.add(ironMesh);
		}

		ItemStack diamondMeshItem = findItem("itemMesh", 4);
		if(diamondMeshItem != null) {
			SieveMeshRegistryEntry diamondMesh = new SieveMeshRegistryEntry(diamondMeshItem);
			diamondMesh.setMeshLevel(4);
			diamondMesh.setHeavy(true);
			diamondMesh.setSpriteLocation(new ResourceLocation(ExCompressum.MOD_ID, "blocks/diamond_mesh"));
			SieveMeshRegistry.add(diamondMesh);
		}

		ExRegistro.instance = this;
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
				//noinspection ConstantConditions /// @Nullable
				if(siftable.getDrop().getItem() == null) {
					ExCompressum.logger.error("Tried to generate Heavy Sieve rewards from a null reward entry: {}", sourceStack.getItem().getRegistryName());
					continue;
				}
				for (int i = 0; i < count; i++) {
					rewards.add(new HeavySieveReward(siftable.getDrop().getItemStack(), siftable.getChance(), sieveLuckMultiplier, siftable.getMeshLevel()));
				}
			}
			return rewards;
		}
		return Collections.emptyList();
	}

	@Nullable
	private ItemStack findItem(String name, int withMetadata) {
		ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_ADSCENSIO, name);
		Item item = Item.REGISTRY.getObject(location);
		if(item != null) {
			return new ItemStack(item, 1, withMetadata);
		}
		return null;
	}

	@Nullable
	private ItemStack findBlock(String name, int withMetadata) {
		ResourceLocation location = new ResourceLocation(Compat.EXNIHILO_ADSCENSIO, name);
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
		return HammerRegistry.registered(state.getBlock());
	}

	@Override
	public Collection<ItemStack> rollHammerRewards(IBlockState state, int miningLevel, float luck, Random rand) {
		List<HammerReward> rewards = HammerRegistry.getRewards(state, miningLevel);
		if(rewards != null) {
			List<ItemStack> list = Lists.newArrayList();
			for(HammerReward reward : rewards) {
				//noinspection ConstantConditions /// @Nullable
				if(reward.getStack().getItem() == null) {
					ExCompressum.logger.error("Tried to roll hammer rewards from a null reward entry: {} (base chance: {}, luck: {}, mining level: {})", state.getBlock().getRegistryName(), reward.getChance(), reward.getFortuneChance(), reward.getMiningLevel());
					continue;
				}
				float fortuneModifier = reward.getFortuneChance();
				float chance = reward.getChance() + reward.getFortuneChance() * luck;
				if(rand.nextFloat() <= chance) {
					list.add(reward.getStack().copy());
				}
			}
			return list;
		}
		return Collections.emptyList();
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
				//noinspection ConstantConditions /// @Nullable
				if(reward.getDrop().getItem() == null) {
					ExCompressum.logger.error("Tried to roll sieve rewards from a null reward entry: {} (base chance: {}, mesh level: {})", state.getBlock().getRegistryName(), reward.getChance(), reward.getMeshLevel());
					continue;
				}
				if(sieveMesh.getMeshLevel() == reward.getMeshLevel() && rand.nextDouble() < (double) reward.getChance() + sieveLuckMultiplier * luck) { // NOTE Sieve Rewards in Adscensio have no luck modifier at the moment
					list.add(reward.getDrop().getItemStack());
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
				//noinspection ConstantConditions /// @Nullable
				if(reward.getStack().getItem() == null) {
					ExCompressum.logger.error("Tried to roll crook rewards from a null reward entry: {} (base chance: {}, luck: {})", state.getBlock().getRegistryName(), reward.getChance(), reward.getFortuneChance());
					continue;
				}
				if(rand.nextFloat() <= reward.getChance() + reward.getFortuneChance() * luck) {
					list.add(reward.getStack().copy());
				}
			}
			return list;
		}
		return Collections.emptyList();
	}

	@Override
	public void loadConfig(Configuration config) {
		sieveLuckMultiplier = config.getFloat("Sieve Luck Multiplier", "compat.exnihiloadscensio", sieveLuckMultiplier, 0f, 10f, "Sieve rewards in Adscensio do not have a luck multiplier at the moment. For fortune to work in Auto Sieves, this default value is applied to *all* rewards when sifting.");
	}

	@Override
	public void postInit() {
		if(ExCompressumConfig.enableWoodChippings) {
			for(IBlockState state : Blocks.LOG.getBlockState().getValidStates()) {
				HammerRegistry.addHammerRecipe(state, new ItemStack(ModItems.woodChipping), 0, 1f, 0f);
				HammerRegistry.addHammerRecipe(state, new ItemStack(ModItems.woodChipping), 0, 0.75f, 0f);
				HammerRegistry.addHammerRecipe(state, new ItemStack(ModItems.woodChipping), 0, 0.5f, 0f);
				HammerRegistry.addHammerRecipe(state, new ItemStack(ModItems.woodChipping), 0, 0.25f, 0f);
			}

			for(IBlockState state : Blocks.LOG2.getBlockState().getValidStates()) {
				HammerRegistry.addHammerRecipe(state, new ItemStack(ModItems.woodChipping), 0, 1f, 0f);
				HammerRegistry.addHammerRecipe(state, new ItemStack(ModItems.woodChipping), 0, 0.75f, 0f);
				HammerRegistry.addHammerRecipe(state, new ItemStack(ModItems.woodChipping), 0, 0.5f, 0f);
				HammerRegistry.addHammerRecipe(state, new ItemStack(ModItems.woodChipping), 0, 0.25f, 0f);
			}

			List<ItemStack> oreDictStacks = OreDictionary.getOres("dustWood", false);
			for(ItemStack itemStack : oreDictStacks) {
				CompostRegistry.register(itemStack.getItem(), itemStack.getItemDamage(), 0.125f, Blocks.DIRT.getDefaultState(), new Color(0xFFC77826));
			}
		}
	}

	@Override
	public void serverStarted(FMLServerStartedEvent event) {

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void clientInit() {

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
		return NihiloMod.ADSCENSIO;
	}
}
