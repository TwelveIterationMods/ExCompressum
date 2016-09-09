package net.blay09.mods.excompressum.compat.exnihiloadscensio;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import exnihiloadscensio.registries.CompostRegistry;
import exnihiloadscensio.registries.HammerRegistry;
import exnihiloadscensio.registries.HammerReward;
import exnihiloadscensio.registries.SieveRegistry;
import exnihiloadscensio.registries.types.Siftable;
import exnihiloadscensio.texturing.Color;
import exnihiloadscensio.util.BlockInfo;
import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.StupidUtils;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.compat.SieveModelBounds;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
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
public class ExNihiloAdscensioAddon implements ExNihiloProvider, IAddon {

	private final EnumMap<NihiloItems, ItemStack> itemMap = Maps.newEnumMap(NihiloItems.class);

	private static final float SIEVE_LUCK_MODIFIER = 2.5f;

	private final SieveModelBounds bounds;

	public ExNihiloAdscensioAddon() {
		itemMap.put(NihiloItems.HAMMER_WOODEN, findItem("hammerWood", 0));
		itemMap.put(NihiloItems.HAMMER_STONE, findItem("hammerStone", 0));
		itemMap.put(NihiloItems.HAMMER_IRON, findItem("hammerIron", 0));
		itemMap.put(NihiloItems.HAMMER_GOLD, findItem("hammerGold", 0));
		itemMap.put(NihiloItems.HAMMER_DIAMOND, findItem("hammerDiamond", 0));
		itemMap.put(NihiloItems.CROOK_WOODEN, findItem("crookWood", 0));
		itemMap.put(NihiloItems.SILK_MESH, findItem("itemMesh", 0));

		itemMap.put(NihiloItems.DUST, findBlock("blockDust", 0));
		itemMap.put(NihiloItems.SIEVE, findBlock("blockSieve", 0)); // TODO Adscensio only has an Oak Sieve at the moment
		itemMap.put(NihiloItems.INFESTED_LEAVES, findBlock("blockInfestedLeaves", 0));

		bounds = new SieveModelBounds(0.8125f, 0.0625f, 0.88f, 0.15625f);

		ExRegistro.instance = this;
	}

	@Override
	public SieveModelBounds getSieveBounds() {
		return bounds;
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
		return SieveRegistry.canBeSifted(StupidUtils.getItemStackFromState(state));
	}

	@Override
	public Collection<ItemStack> rollSieveRewards(IBlockState state, int meshLevel, float luck, Random rand) {
		List<Siftable> rewards = SieveRegistry.getDrops(new BlockInfo(state));
		if(rewards != null) {
			List<ItemStack> list = Lists.newArrayList();
			for(Siftable reward : rewards) {
				if(meshLevel == reward.getMeshLevel() && rand.nextDouble() < (double) reward.getChance() + SIEVE_LUCK_MODIFIER * luck) { // TODO Sieve Rewards in Adscensio have no luck modifier at the moment; I randomly picked 2.5f for now. Balance it, maybe config it too.
					list.add(reward.getDrop().getItemStack());
				}
			}
			return list;
		}
		return Collections.emptyList();
	}

	@Nullable
	@Override
	public ItemStack rollSilkWorm(EntityLivingBase player, IBlockState state, int fortune) {
		return null; // TODO Adscensio has no silk worms yet.
	}

	@Override
	public void loadConfig(Configuration config) {

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
}
