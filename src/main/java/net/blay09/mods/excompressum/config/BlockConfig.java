package net.blay09.mods.excompressum.config;

import com.google.common.collect.Maps;
import net.blay09.mods.excompressum.block.BlockBait;
import net.blay09.mods.excompressum.block.BlockCompressed;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Collection;
import java.util.Map;

public class BlockConfig {

	public static class Entry {
		private final String name;
		private final ItemStack itemStack;
		private boolean defaultValue;
		private boolean enabled;

		public Entry(String name, ItemStack itemStack, boolean defaultValue) {
			this.name = name;
			this.itemStack = itemStack;
			this.defaultValue = defaultValue;
		}

		public String getName() {
			return name;
		}

		public ItemStack getItemStack() {
			return itemStack;
		}

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		public boolean getDefaultValue() {
			return defaultValue;
		}
	}

	private static final Map<String, Entry> entries = Maps.newHashMap();

	private static void addEntry(String name, ItemStack itemStack, boolean defaultValue) {
		entries.put(name, new Entry(name, itemStack, defaultValue));
	}

	public static Collection<Entry> getEntries() {
		return entries.values();
	}

	public static boolean isEnabled(String name) {
		Entry entry = entries.get(name);
		return entry != null && entry.isEnabled();
	}

	public static void postLoad(Configuration config) {
		addEntry("Heavy Sieve", new ItemStack(ModBlocks.heavySieve, 0, OreDictionary.WILDCARD_VALUE), true);
		addEntry("Wooden Crucible", new ItemStack(ModBlocks.woodenCrucible, 0, OreDictionary.WILDCARD_VALUE), true);

		addEntry("Auto Compressor", new ItemStack(ModBlocks.autoCompressor), true);
		addEntry("Auto Hammer", new ItemStack(ModBlocks.autoHammer), true);
		addEntry("Auto Compressed Hammer", new ItemStack(ModBlocks.autoCompressedHammer), true);
		addEntry("Auto Sieve", new ItemStack(ModBlocks.autoSieve), true);
		addEntry("Auto Heavy Sieve", new ItemStack(ModBlocks.autoHeavySieve), true);

		addEntry("Mana Sieve", new ItemStack(ModBlocks.manaSieve), true);

		addEntry("Compressed Dust", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DUST.ordinal()), true);
		addEntry("Compressed Soul Sand", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SOUL_SAND.ordinal()), true);
		addEntry("Compressed Flint", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.FLINT.ordinal()), true);
		addEntry("Compressed Nether Gravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHER_GRAVEL.ordinal()), true);
		addEntry("Compressed Ender Gravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.ENDER_GRAVEL.ordinal()), true);
		boolean exUtilsLoaded = Loader.isModLoaded(Compat.EXTRAUTILS2);
		addEntry("Compressed Cobblestone", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.COBBLESTONE.ordinal()), !exUtilsLoaded);
		addEntry("Compressed Gravel", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.GRAVEL.ordinal()), !exUtilsLoaded);
		addEntry("Compressed Sand", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SAND.ordinal()), !exUtilsLoaded);
		addEntry("Compressed Dirt", new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DIRT.ordinal()), !exUtilsLoaded);

		addEntry("Wolf Bait", new ItemStack(ModBlocks.bait, 1, BlockBait.Type.WOLF.ordinal()), true);
		addEntry("Ocelot Bait", new ItemStack(ModBlocks.bait, 1, BlockBait.Type.OCELOT.ordinal()), true);
		addEntry("Cow Bait", new ItemStack(ModBlocks.bait, 1, BlockBait.Type.COW.ordinal()), true);
		addEntry("Pig Bait", new ItemStack(ModBlocks.bait, 1, BlockBait.Type.PIG.ordinal()), true);
		addEntry("Chicken Bait", new ItemStack(ModBlocks.bait, 1, BlockBait.Type.CHICKEN.ordinal()), true);
		addEntry("Sheep Bait", new ItemStack(ModBlocks.bait, 1, BlockBait.Type.SHEEP.ordinal()), true);
		addEntry("Squid Bait", new ItemStack(ModBlocks.bait, 1, BlockBait.Type.SQUID.ordinal()), false);

		for (Entry entry : entries.values()) {
			entry.setEnabled(config.getBoolean(entry.getName(), "blocks", entry.getDefaultValue(), "If set to false, the recipe for the " + entry.getName() + " will be disabled (and it won't show up in JEI)."));
		}
	}

}