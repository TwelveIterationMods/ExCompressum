package net.blay09.mods.excompressum.registry.woodencrucible;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.RegistryKey;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class WoodenCrucibleRegistry extends AbstractRegistry {

	public static final WoodenCrucibleRegistry INSTANCE = new WoodenCrucibleRegistry();
	private final Map<RegistryKey, WoodenCrucibleRegistryEntry> entries = Maps.newHashMap();

	public WoodenCrucibleRegistry() {
		super("WoodenCrucible");
	}

	@Nullable
	public static WoodenCrucibleRegistryEntry getEntry(ItemStack itemStack) {
		RegistryKey key = new RegistryKey(itemStack);
		WoodenCrucibleRegistryEntry entry = INSTANCE.entries.get(key);
		if(entry != null) {
			return entry;
		}
		return INSTANCE.entries.get(key.withWildcard());
	}

	public Map<RegistryKey, WoodenCrucibleRegistryEntry> getEntries() {
		return entries;
	}

	private static void add(WoodenCrucibleRegistryEntry entry) {
		RegistryKey key = new RegistryKey(entry.getItemStack());
		if(INSTANCE.entries.containsKey(key)) {
			ExCompressum.logger.error("Duplicate entry for " + key + " in " + INSTANCE.registryName + ", overwriting...");
		}
		INSTANCE.entries.put(key, entry);
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
		custom.addProperty("__comment", "You can define additional items to melt in the Wooden Crucible here. Use * as a wildcard for metadata. Use ore: prefix in name to query the Ore Dictionary. Metadata is ignored for Ore Dictionary entries.");
		JsonObject emptyEntry = new JsonObject();
		emptyEntry.addProperty("name", "");
		emptyEntry.addProperty("metadata", "*");
		emptyEntry.addProperty("fluid", "water");
		emptyEntry.addProperty("amount", 0);

		JsonArray entries = new JsonArray();
		entries.add(emptyEntry);
		custom.add("entries", entries);
		root.add("custom", custom);

		JsonObject example = new JsonObject();
		example.addProperty("__comment", "This example would allow fish to be melted into 100mb of water.");
		example.addProperty("name", "minecraft:fish");
		example.addProperty("metadata", "0");
		example.addProperty("fluid", "water");
		example.addProperty("amount", 100);
		root.add("example", example);

		return root;
	}

	@Override
	protected void loadCustom(JsonObject entry) {
		String name = tryGetString(entry, "name", "");
		if(name.isEmpty()) {
			return;
		}
		ResourceLocation location = new ResourceLocation(name);
		String fluidName = tryGetString(entry, "fluid", "water");
		Fluid fluid = FluidRegistry.getFluid(fluidName);
		if(fluid == null) {
			logUnknownFluid(fluidName, location);
			return;
		}
		int amount = tryGetInt(entry, "amount", 100);
		if(location.getResourceDomain().equals("ore")) { // it would be funny if someone made a mod with mod id "ore"
			if(!addOre(location.getResourcePath(), fluid, amount)) {
				logUnknownOre(location);
			}
		} else {
			Item item = Item.REGISTRY.getObject(location);
			if(item == null) {
				logUnknownItem(location);
				return;
			}
			String metadata = tryGetString(entry, "metadata", "0");
			ItemStack itemStack;
			if(metadata.equals("*")) {
				itemStack = new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
			} else {
				itemStack = new ItemStack(item, 1, tryParseInt(metadata));
			}
			add(new WoodenCrucibleRegistryEntry(itemStack, fluid, amount));
		}
	}

	@Override
	protected void registerDefaults(JsonObject defaults) {
		if(tryGetBoolean(defaults, "minecraft:apple", true)) {
			add(new WoodenCrucibleRegistryEntry(new ItemStack(Items.APPLE), FluidRegistry.WATER, 100));
		}

		if(tryGetBoolean(defaults, "ore:treeSapling", true)) {
			addOre("treeSapling", FluidRegistry.WATER, 100);
		}

		if(tryGetBoolean(defaults, "ore:treeLeaves", true)) {
			addOre("treeLeaves", FluidRegistry.WATER, 250);
		}

		if(tryGetBoolean(defaults, "minecraft:cactus", true)) {
			add(new WoodenCrucibleRegistryEntry(new ItemStack(Blocks.CACTUS), FluidRegistry.WATER, 250));
		}

		if(tryGetBoolean(defaults, "minecraft:yellow_flower", true)) {
			add(new WoodenCrucibleRegistryEntry(new ItemStack(Blocks.YELLOW_FLOWER), FluidRegistry.WATER, 100));
		}

		if(tryGetBoolean(defaults, "minecraft:red_flower", true)) {
			add(new WoodenCrucibleRegistryEntry(new ItemStack(Blocks.RED_FLOWER), FluidRegistry.WATER, 100));
		}

		if(tryGetBoolean(defaults, "ore:listAllfruit", true)) {
			addOre("listAllfruit", FluidRegistry.WATER, 50);
		}
	}

	private boolean addOre(String oreName, Fluid fluid, int amount) {
		List<ItemStack> list = OreDictionary.getOres(oreName, false);
		for(ItemStack itemStack : list) {
			add(new WoodenCrucibleRegistryEntry(itemStack, fluid, amount));
		}
		return list.size() > 0;
	}

}
