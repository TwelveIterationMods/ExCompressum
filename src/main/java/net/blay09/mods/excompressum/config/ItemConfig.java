package net.blay09.mods.excompressum.config;

import com.google.common.collect.Maps;
import net.blay09.mods.excompressum.item.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import java.util.Collection;
import java.util.Map;

public class ItemConfig {

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
		addEntry("Compressed Crook", new ItemStack(ModItems.compressedCrook), true);
		addEntry("Uncompressed Coal", new ItemStack(ModItems.uncompressedCoal), true);
		addEntry("Bat Zapper", new ItemStack(ModItems.batZapper), true);
		addEntry("Ore Smasher", new ItemStack(ModItems.oreSmasher), true);

		addEntry("Compressed Wooden Hammer", new ItemStack(ModItems.compressedHammerWood), true);
		addEntry("Compressed Stone Hammer", new ItemStack(ModItems.compressedHammerStone), true);
		addEntry("Compressed Iron Hammer", new ItemStack(ModItems.compressedHammerIron), true);
		addEntry("Compressed Gold Hammer", new ItemStack(ModItems.compressedHammerGold), true);
		addEntry("Compressed Diamond Hammer", new ItemStack(ModItems.compressedHammerDiamond), true);

		addEntry("Chicken Stick", new ItemStack(ModItems.chickenStick), true);

		for (Entry entry : entries.values()) {
			entry.setEnabled(config.getBoolean(entry.getName(), "items", entry.getDefaultValue(), "If set to false, the recipe for the " + entry.getName() + " will be disabled (and it won't show up in JEI)."));
		}
	}

}
