package net.blay09.mods.excompressum.registry.woodencrucible;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ReloadRegistryEvent;
import net.blay09.mods.excompressum.api.woodencrucible.WoodenCrucibleRegistryEntry;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class WoodenCrucibleRegistry extends AbstractRegistry {

    public static final WoodenCrucibleRegistry INSTANCE = new WoodenCrucibleRegistry();
    private final Map<ResourceLocation, WoodenCrucibleRegistryEntry> entries = new HashMap<>();

    public WoodenCrucibleRegistry() {
        super("WoodenCrucible");
    }

    @Nullable
    public static WoodenCrucibleRegistryEntry getEntry(ItemStack itemStack) {
        return INSTANCE.entries.get(itemStack.getItem().getRegistryName());
    }

    public Map<ResourceLocation, WoodenCrucibleRegistryEntry> getEntries() {
        return entries;
    }

    public void add(WoodenCrucibleRegistryEntry entry) {
        final ResourceLocation registryName = entry.getItemStack().getItem().getRegistryName();
        if (INSTANCE.entries.containsKey(registryName)) {
            ExCompressum.logger.error("Duplicate entry for " + registryName + " in " + INSTANCE.registryName + ", overwriting...");
        }
        INSTANCE.entries.put(registryName, entry);
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
        if (name.isEmpty()) {
            return;
        }
        ResourceLocation location = new ResourceLocation(name);
        ResourceLocation fluidName = new ResourceLocation(tryGetString(entry, "fluid", "water"));
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidName);
        if (fluid == null) {
            logUnknownFluid(fluidName.toString(), location);
            return;
        }
        int amount = tryGetInt(entry, "amount", 100);
        if (location.getNamespace().equals("ore")) { // it would be funny if someone made a mod with mod id "ore"
            if (!addOre(location.getPath(), fluid, amount)) {
                logUnknownOre(location);
            }
        } else {
            Item item = ForgeRegistries.ITEMS.getValue(location);
            if (item == null) {
                logUnknownItem(location);
                return;
            }
            ItemStack itemStack = new ItemStack(item);
            add(new WoodenCrucibleRegistryEntry(itemStack, fluid, amount));
        }
    }

    @Override
    protected void registerDefaults(JsonObject defaults) {
        if (tryGetBoolean(defaults, "minecraft:apple", true)) {
            add(new WoodenCrucibleRegistryEntry(new ItemStack(Items.APPLE), Fluids.WATER, 100));
        }

        if (tryGetBoolean(defaults, "ore:treeSapling", true)) {
            addOre("treeSapling", Fluids.WATER, 100);
        }

        if (tryGetBoolean(defaults, "ore:treeLeaves", true)) {
            addOre("treeLeaves", Fluids.WATER, 250);
        }

        if (tryGetBoolean(defaults, "minecraft:cactus", true)) {
            add(new WoodenCrucibleRegistryEntry(new ItemStack(Blocks.CACTUS), Fluids.WATER, 250));
        }

        if (tryGetBoolean(defaults, "minecraft:yellow_flower", true)) {
            add(new WoodenCrucibleRegistryEntry(new ItemStack(Blocks.DANDELION), Fluids.WATER, 100));
        }

        if (tryGetBoolean(defaults, "minecraft:red_flower", true)) {
            add(new WoodenCrucibleRegistryEntry(new ItemStack(Blocks.POPPY), Fluids.WATER, 100));
        }

        if (tryGetBoolean(defaults, "ore:listAllfruit", true)) {
            addOre("listAllfruit", Fluids.WATER, 50);
        }
    }

    @Override
    protected ReloadRegistryEvent getRegistryEvent() {
        return new ReloadRegistryEvent.WoodenCrucible();
    }

    private boolean addOre(String oreName, Fluid fluid, int amount) {
		/*List<ItemStack> list = OreDictionary.getOres(oreName, false);
		for(ItemStack itemStack : list) {
			add(new WoodenCrucibleRegistryEntry(itemStack, fluid, amount));
		}
		return list.size() > 0;*/
        return false;
    }

}
