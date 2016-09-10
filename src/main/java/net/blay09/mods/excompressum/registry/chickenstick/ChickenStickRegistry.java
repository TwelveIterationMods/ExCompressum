package net.blay09.mods.excompressum.registry.chickenstick;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.RegistryKey;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ChickenStickRegistry extends AbstractRegistry {

    public static final ChickenStickRegistry INSTANCE = new ChickenStickRegistry();
    private final List<RegistryKey> validBlocks = Lists.newArrayList();

    public ChickenStickRegistry() {
        super("ChickenStickRegistry");
    }

    public void add(IBlockState state, boolean isWildcard) {
        validBlocks.add(new RegistryKey(state, isWildcard));
    }

    public void add(ItemStack itemStack) {
        validBlocks.add(new RegistryKey(itemStack));
    }

    public static boolean isHammerable(IBlockState state) {
        RegistryKey key = new RegistryKey(state, false);
        return INSTANCE.validBlocks.contains(key) || INSTANCE.validBlocks.contains(key.withWildcard());
    }

    @Override
    public void registerDefaults(JsonObject defaults) {
        if(tryGetBoolean(defaults, "minecraft:cobblestone", true)) {
            add(Blocks.COBBLESTONE.getDefaultState(), false);
        }

        if(tryGetBoolean(defaults, "minecraft:gravel", true)) {
            add(Blocks.GRAVEL.getDefaultState(), false);
        }

        if(tryGetBoolean(defaults, "minecraft:sand", true)) {
            add(Blocks.SAND.getDefaultState(), false);
        }
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
        if(name.isEmpty()) {
            return;
        }
        ResourceLocation location = new ResourceLocation(name);
        if(location.getResourceDomain().equals("ore")) {
            if(!addOre(location.getResourcePath())) {
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
            add(itemStack);
        }
    }

    private boolean addOre(String oreName) {
        List<ItemStack> list = OreDictionary.getOres(oreName, false);
        for(ItemStack itemStack : list) {
            add(itemStack);
        }
        return list.size() > 0;
    }
}
