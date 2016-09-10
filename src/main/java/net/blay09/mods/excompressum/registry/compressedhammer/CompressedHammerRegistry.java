package net.blay09.mods.excompressum.registry.compressedhammer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.StupidUtils;
import net.blay09.mods.excompressum.block.BlockCompressed;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.RegistryKey;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CompressedHammerRegistry extends AbstractRegistry {

	public static final CompressedHammerRegistry INSTANCE = new CompressedHammerRegistry();
	private Map<RegistryKey, CompressedHammerRegistryEntry> entries = Maps.newHashMap();

	public CompressedHammerRegistry() {
		super("CompressedHammer");
	}

	public Map<RegistryKey, CompressedHammerRegistryEntry> getEntries() {
		return entries;
	}

	public void add(CompressedHammerRegistryEntry entry) {
		RegistryKey key = new RegistryKey(entry.getInputState(), entry.isWildcard());
		CompressedHammerRegistryEntry previousEntry = entries.get(key);
		if(previousEntry != null) {
			for(CompressedHammerReward reward : entry.getRewards()) {
				previousEntry.addReward(reward);
			}
		} else {
			entries.put(key, entry);
		}
	}

	@Nullable
	public static CompressedHammerRegistryEntry getEntryForBlockState(IBlockState state) {
		RegistryKey key = new RegistryKey(state, false);
		CompressedHammerRegistryEntry entry = INSTANCE.entries.get(key);
		if(entry == null) {
			return INSTANCE.entries.get(key.withWildcard());
		}
		return entry;
	}

	public static boolean isHammerable(IBlockState state) {
		return getEntryForBlockState(state) != null;
	}

	public static boolean isHammerable(ItemStack itemStack) {
		IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
		return state != null && isHammerable(state);
	}

	public static Collection<ItemStack> rollHammerRewards(IBlockState state, float luck, Random rand) {
		CompressedHammerRegistryEntry entry = getEntryForBlockState(state);
		if(entry != null) {
			List<ItemStack> list = Lists.newArrayList();
			for(CompressedHammerReward reward : entry.getRewards()) {
				float chance = reward.getBaseChance() + reward.getLuckMultiplier() * luck;
				if(rand.nextFloat() < chance) {
					list.add(reward.getItemStack().copy());
				}
			}
			return list;
		}
		return Collections.emptyList();
	}

	public static Collection<ItemStack> rollHammerRewards(ItemStack itemStack, float luck, Random rand) {
		IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
		if(state != null) {
			return rollHammerRewards(state, luck, rand);
		}
		return Collections.emptyList();
	}

	@Override
	protected void clear() {
		entries.clear();
	}

	@Override
	protected JsonObject create() {
		JsonObject root = new JsonObject();

		JsonObject defaults = new JsonObject();
		defaults.addProperty("__comment", "You can disable defaults by setting these to false. Do *NOT* try to add additional entries here. You can add additional entries in the custom section.");
		root.add("defaults", defaults);

		JsonObject custom = new JsonObject();
		custom.addProperty("__comment", "You can define additional blocks to be hammered by Compressed Hammers here. Use * as a wildcard for metadata. Use prefix ore: in name to query the Ore Dictionary.");
		JsonObject emptyEntry = new JsonObject();
		emptyEntry.addProperty("name", "");
		emptyEntry.addProperty("metadata", "*");
		JsonArray rewards = new JsonArray();
		JsonObject reward = new JsonObject();
		reward.addProperty("name", "");
		reward.addProperty("count", 1);
		reward.addProperty("metadata", "0");
		reward.addProperty("chance", 1f);
		reward.addProperty("luck", 0f);
		rewards.add(reward);
		emptyEntry.add("rewards", rewards);

		JsonArray entries = new JsonArray();
		entries.add(emptyEntry);
		custom.add("entries", entries);

		JsonObject example = new JsonObject();
		example.addProperty("__comment", "This example would allow Obsidian to be hammered into nine diamonds using a Compressed Hammer.");
		example.addProperty("name", "minecraft:obsidian");
		example.addProperty("metadata", "0");
		rewards = new JsonArray();
		reward = new JsonObject();
		reward.addProperty("__comment", "Chance is a floating point value (1.0 equals 100%). Luck is the multiplier that scales the hammers fortune level, applied to the base chance.");
		reward.addProperty("name", "minecraft:diamond");
		reward.addProperty("count", 9);
		reward.addProperty("metadata", 0);
		reward.addProperty("chance", 1f);
		reward.addProperty("luck", 0f);
		rewards.add(reward);
		example.add("rewards", rewards);
		custom.add("example", example);

		root.add("custom", custom);

		return root;
	}

	@Override
	protected void loadCustom(JsonObject entry) {
		String name = tryGetString(entry, "name", "");
		if(name.isEmpty()) {
			return;
		}
		ResourceLocation location = new ResourceLocation(name);
		JsonArray rewards = tryGetArray(entry, "rewards");
		List<CompressedHammerReward> rewardList = Lists.newArrayListWithCapacity(rewards.size());
		for(int i = 0; i < rewards.size(); i++) {
			JsonElement element = rewards.get(i);
			if(element.isJsonObject()) {
				JsonObject reward = element.getAsJsonObject();
				String rewardName = tryGetString(reward, "name", "");
				if(rewardName.isEmpty()) {
					continue;
				}
				ResourceLocation rewardLocation = new ResourceLocation(rewardName);
				Item item = Item.REGISTRY.getObject(rewardLocation);
				if(item == null) {
					logUnknownItem(rewardLocation);
					continue;
				}
				int count = tryGetInt(reward, "count", 1);
				int metadata = tryGetInt(reward, "metadata", 0);
				float chance = tryGetFloat(reward, "chance", 1f);
				if(chance > 1f) {
					ExCompressum.logger.warn("Reward chance is out of range for {} in {}, capping at 1.0...", rewardLocation, registryName);
					chance = 1f;
				}
				float luckMultiplier = tryGetFloat(reward, "luck", 0f);
				rewardList.add(new CompressedHammerReward(new ItemStack(item, count, metadata), chance, luckMultiplier));
			} else {
				throw new ClassCastException("rewards must be an array of json objects in " + registryName);
			}
		}
		if(location.getResourceDomain().equals("ore")) {
			if(!addOre(location.getResourcePath(), rewardList)) {
				logUnknownOre(location);
			}
		} else {
			Item item = Item.REGISTRY.getObject(location);
			if(item == null) {
				logUnknownItem(location);
				return;
			}
			ItemStack itemStack;
			String metadata = tryGetString(entry, "metadata", "0");
			if (metadata.equals("*")) {
				itemStack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
			} else {
				itemStack = new ItemStack(item, 1, tryParseInt(metadata));
			}
			IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
			if(state != null) {
				CompressedHammerRegistryEntry newEntry = new CompressedHammerRegistryEntry(state, itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE);
				for(CompressedHammerReward reward : rewardList) {
					newEntry.addReward(reward);
				}
				add(newEntry);
			} else {
				ExCompressum.logger.warn("Entry {} could not be registered for {}; it's not a block", location, registryName);
			}
		}
	}

	private boolean addOre(String oreName, List<CompressedHammerReward> rewards) {
		List<ItemStack> list = OreDictionary.getOres(oreName, false);
		for(ItemStack itemStack : list) {
			IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
			if(state != null) {
				CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(state, itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE);
				for(CompressedHammerReward reward : rewards) {
					entry.addReward(reward);
				}
				add(entry);
			} else {
				ExCompressum.logger.warn("Ore dictionary entry {} in {} could not be registered for {}; it's not a block", itemStack.getItem().getRegistryName(), oreName, registryName);
			}
		}
		return list.size() > 0;
	}

	@Override
	protected void registerDefaults(JsonObject defaults) {
		if(tryGetBoolean(defaults, "excompressum:compressed_cobblestone", true)) {
			CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(ModBlocks.compressedBlock.getDefaultState()
					.withProperty(BlockCompressed.VARIANT, BlockCompressed.Type.COBBLESTONE), false);
			entry.addReward(new CompressedHammerReward(new ItemStack(Blocks.GRAVEL, 9), 1f, 0f));
			add(entry);
		}

		if(tryGetBoolean(defaults, "excompressum:compressed_gravel", true)) {
			CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(ModBlocks.compressedBlock.getDefaultState()
					.withProperty(BlockCompressed.VARIANT, BlockCompressed.Type.GRAVEL), false);
			entry.addReward(new CompressedHammerReward(new ItemStack(Blocks.SAND, 9), 1f, 0f));
			add(entry);
		}

		if(tryGetBoolean(defaults, "excompressum:compressed_sand", true)) {
			ItemStack dustBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.DUST);
			if(dustBlock != null) {
				CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(ModBlocks.compressedBlock.getDefaultState()
						.withProperty(BlockCompressed.VARIANT, BlockCompressed.Type.SAND), false);
				entry.addReward(new CompressedHammerReward(ItemHandlerHelper.copyStackWithSize(dustBlock, 9), 1f, 0f));
				add(entry);
			}
		}

		if(tryGetBoolean(defaults, "ExtraUtils2:CompressedCobblestone", true)) {
			ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "CompressedCobblestone");
			if(Block.REGISTRY.containsKey(location)) {
				Block exUtilsBlock = Block.REGISTRY.getObject(location);
				CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(exUtilsBlock.getDefaultState(), false);
				entry.addReward(new CompressedHammerReward(new ItemStack(Blocks.GRAVEL, 9), 1f, 0f));
				add(entry);
			}
		}

		if(tryGetBoolean(defaults, "ExtraUtils2:CompressedGravel", true)) {
			ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "CompressedGravel");
			if(Block.REGISTRY.containsKey(location)) {
				Block exUtilsBlock = Block.REGISTRY.getObject(location);
				CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(exUtilsBlock.getDefaultState(), false);
				entry.addReward(new CompressedHammerReward(new ItemStack(Blocks.SAND, 9), 1f, 0f));
				add(entry);
			}
		}

		if(tryGetBoolean(defaults, "ExtraUtils2:CompressedSand", true)) {
			ItemStack dustBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.DUST);
			if(dustBlock != null) {
				ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "CompressedSand");
				if (Block.REGISTRY.containsKey(location)) {
					Block exUtilsBlock = Block.REGISTRY.getObject(location);
					CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(exUtilsBlock.getDefaultState(), false);
					entry.addReward(new CompressedHammerReward(ItemHandlerHelper.copyStackWithSize(dustBlock, 9), 1f, 0f));
					add(entry);
				}
			}
		}
	}

}
