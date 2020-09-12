package net.blay09.mods.excompressum.registry.chickenstick;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.api.ReloadRegistryEvent;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ChickenStickRegistry extends AbstractRegistry {

    public static final ChickenStickRegistry INSTANCE = new ChickenStickRegistry();
    private final List<ResourceLocation> entries = Lists.newArrayList();

    public ChickenStickRegistry() {
        super("ChickenStickRegistry");
    }

    public void add(BlockState state) {
        entries.add(state.getBlock().getRegistryName());
    }

    public void add(ItemStack itemStack) {
        entries.add(itemStack.getItem().getRegistryName());
    }

    public List<ResourceLocation> getEntries() {
        return entries;
    }

    public static boolean isHammerable(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return INSTANCE.entries.contains(registryName);
    }

    @Override
    public void registerDefaults(JsonObject defaults) {
        if (tryGetBoolean(defaults, "minecraft:cobblestone", true)) {
            add(Blocks.COBBLESTONE.getDefaultState());
        }

        if (tryGetBoolean(defaults, "minecraft:gravel", true)) {
            add(Blocks.GRAVEL.getDefaultState());
        }

        if (tryGetBoolean(defaults, "minecraft:sand", true)) {
            add(Blocks.SAND.getDefaultState());
        }
    }

    @Override
    protected ReloadRegistryEvent getRegistryEvent() {
        return new ReloadRegistryEvent.ChickenStick();
    }

    @Override
    protected void clear() {
        entries.clear();
    }

    @Override
    public JsonObject create() {
        JsonObject root = new JsonObject();

        JsonObject defaults = new JsonObject();
        defaults.addProperty("__comment", "You can disable defaults by setting these to false. Do *NOT* try to add additional entries here. You can add additional entries in the custom section.");
        root.add("defaults", defaults);

        JsonObject custom = new JsonObject();
        custom.addProperty("__comment", "By default, the chicken stick is limited as to what it can hammer. You can define additional blocks here. Use * as a wildcard for metadata.");
        JsonObject emptyEntry = new JsonObject();
        emptyEntry.addProperty("name", "");
        emptyEntry.addProperty("metadata", "*");

        JsonArray entries = new JsonArray();
        entries.add(emptyEntry);
        custom.add("entries", entries);

        JsonObject example = new JsonObject();
        example.addProperty("__comment", "This example would allow smooth Stone to be hammered with the Chicken Stick.");
        example.addProperty("name", "minecraft:stone");
        example.addProperty("metadata", "0");
        custom.add("example", example);

        root.add("custom", custom);

        return root;
    }

    @Override
    public void loadCustom(JsonObject entry) {
        String name = tryGetString(entry, "name", "");
        if (name.isEmpty()) {
            return;
        }
        ResourceLocation location = new ResourceLocation(name);
        if (location.getNamespace().equals("ore")) {
            if (!addOre(location.getPath())) {
                logUnknownOre(location);
            }
        } else {
            Item item = ForgeRegistries.ITEMS.getValue(location);
            if (item == null) {
                logUnknownItem(location);
                return;
            }
            ItemStack itemStack = new ItemStack(item);
            add(itemStack);
        }
    }

    private boolean addOre(String oreName) {
        /*List<ItemStack> list = OreDictionary.getOres(oreName, false);
        for(ItemStack itemStack : list) {
            add(itemStack);
        }
        return list.size() > 0;*/
        return false;
    }
}
