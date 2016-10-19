package net.blay09.mods.excompressum.registry.heavysieve;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.block.BlockCompressed;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.RegistryKey;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HeavySieveRegistry extends AbstractRegistry {

    public static final HeavySieveRegistry INSTANCE = new HeavySieveRegistry();
    private final Map<RegistryKey, HeavySieveRegistryEntry> entries = Maps.newHashMap();
    private static final int DEFAULT_LOSS = 2;

    private int defaultLoss;

    public HeavySieveRegistry() {
        super("HeavySieve");
    }

    @Nullable
    public static HeavySieveRegistryEntry getEntryForBlockState(IBlockState state, boolean withWildcard) {
        return INSTANCE.entries.get(new RegistryKey(state, withWildcard));
    }

    public static boolean isSiftable(IBlockState state) {
        RegistryKey key = new RegistryKey(state, false);
        return INSTANCE.entries.get(key) != null || INSTANCE.entries.get(key.withWildcard()) != null;
    }

    public static boolean isSiftableWithMesh(IBlockState state, SieveMeshRegistryEntry sieveMesh) {
        if(ExRegistro.doMeshesSplitLootTables()) {
            RegistryKey key = new RegistryKey(state, false);
            HeavySieveRegistryEntry entry = INSTANCE.entries.get(key);
            if(entry != null && !entry.getRewardsForMesh(sieveMesh).isEmpty()) {
                return true;
            }
            HeavySieveRegistryEntry wildcardEntry = INSTANCE.entries.get(key.withWildcard());
            return wildcardEntry != null && !wildcardEntry.getRewardsForMesh(sieveMesh).isEmpty();
        }
        return isSiftable(state);
    }

    public static boolean isSiftableWithMesh(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh) {
        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && isSiftableWithMesh(state, sieveMesh);
    }

    public static boolean isSiftable(ItemStack itemStack) {
        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && isSiftable(state);
    }

    public void add(HeavySieveRegistryEntry entry) {
        RegistryKey key = new RegistryKey(entry.getInputState(), entry.isWildcard());
        HeavySieveRegistryEntry previousEntry = entries.get(key);
        if(previousEntry != null) {
            for(HeavySieveReward reward : entry.getRewards()) {
                previousEntry.addReward(reward);
            }
        } else {
            entries.put(key, entry);
        }
    }

    public Map<RegistryKey, HeavySieveRegistryEntry> getEntries() {
        return entries;
    }

    public static Collection<ItemStack> rollSieveRewards(IBlockState state, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        List<ItemStack> list = Lists.newArrayList();
        RegistryKey key = new RegistryKey(state, false);
        HeavySieveRegistryEntry entry = INSTANCE.entries.get(key);
        if(entry != null) {
            rollSieveRewardsToList(entry, list, sieveMesh, luck, rand);
        }
        HeavySieveRegistryEntry wildcardEntry = INSTANCE.entries.get(key.withWildcard());
        if(wildcardEntry != null) {
            rollSieveRewardsToList(wildcardEntry, list, sieveMesh, luck, rand);
        }
        return list;
    }

    public static Collection<ItemStack> rollSieveRewards(ItemStack itemStack, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
        if(state != null) {
            return rollSieveRewards(state, sieveMesh, luck, rand);
        }
        return Collections.emptyList();
    }

    private static void rollSieveRewardsToList(HeavySieveRegistryEntry entry, List<ItemStack> list, SieveMeshRegistryEntry sieveMesh, float luck, Random rand) {
        for(HeavySieveReward reward : entry.getRewardsForMesh(sieveMesh)) {
            if(rand.nextFloat() < reward.getBaseChance() + reward.getLuckMultiplier() * luck) {
                list.add(reward.getItemStack().copy());
            }
        }
    }

    @Override
    protected void clear() {
        entries.clear();
        defaultLoss = DEFAULT_LOSS;
    }

    @Override
    protected JsonObject create() {
        JsonObject root = new JsonObject();

        JsonObject options = new JsonObject();
        options.addProperty("__comment", "These options will affect the auto-generation of sieve rewards for compressed blocks. You can turn the auto-generation off individually in the defaults section.");
        root.add("options", options);

        JsonObject defaults = new JsonObject();
        defaults.addProperty("__comment", "You can disable defaults by setting these to false. Do *NOT* try to add additional entries here. You can add additional entries in the custom section.");
        root.add("defaults", defaults);

        JsonObject custom = new JsonObject();
        custom.addProperty("__comment", "You can define additional blocks to sift in the Heavy Sieve here. Use * as a wildcard for metadata. Ure ore: prefix in name to query the Ore Dictionary. Mesh Level is only used for Adscensio.");

        JsonArray entries = new JsonArray();

        JsonObject emptyEntry = new JsonObject();
        emptyEntry.addProperty("name", "");
        emptyEntry.addProperty("metadata", "*");
        emptyEntry.addProperty("type", "list");
        JsonArray rewards = new JsonArray();
        JsonObject reward = new JsonObject();
        reward.addProperty("name", "");
        reward.addProperty("metadata", 0);
        reward.addProperty("count", 1);
        reward.addProperty("chance", 1f);
        reward.addProperty("luck", 0f);
        rewards.add(reward);
        emptyEntry.add("rewards", rewards);
        entries.add(emptyEntry);

        emptyEntry = new JsonObject();
        emptyEntry.addProperty("name", "");
        emptyEntry.addProperty("metadata", "*");
        emptyEntry.addProperty("type", "generate");
        emptyEntry.addProperty("source", "");
        emptyEntry.addProperty("sourceMetadata", "*");
        emptyEntry.addProperty("sourceCount", 9);
        entries.add(emptyEntry);

        custom.add("entries", entries);

        JsonObject example = new JsonObject();
        example.addProperty("__comment", "This example would allow Double Compressed Gravel to be sifted in a Heavy Sieve into 64x the rewards of normal gravel.");
        example.addProperty("name", "ExUtils2:CompressedGravel");
        example.addProperty("metadata", "1");
        example.addProperty("type", "generate");
        example.addProperty("source", "minecraft:gravel");
        example.addProperty("sourceMetadata", "*");
        example.addProperty("sourceCount", 64);
        custom.add("example_generate", example);

        example = new JsonObject();
        example.addProperty("__comment", "This example would allow clay to be sifted in a Heavy Sieve into 6 diamonds.");
        example.addProperty("name", "ExUtils2:CompressedGravel");
        example.addProperty("metadata", "1");
        example.addProperty("type", "list");
        rewards = new JsonArray();
        reward = new JsonObject();
        reward.addProperty("name", "minecraft:diamond");
        reward.addProperty("metadata", 0);
        reward.addProperty("count", 6);
        reward.addProperty("chance", 1f);
        reward.addProperty("luck", 0f);
        rewards.add(reward);
        example.add("rewards", rewards);
        custom.add("example_list", example);

        root.add("custom", custom);

        return root;
    }

    @Override
    protected boolean hasOptions() {
        return true;
    }

    @Override
    protected void loadOptions(JsonObject entry) {
        defaultLoss = tryGetInt(entry, "loss for default generated rewards (out of 9)", DEFAULT_LOSS);
        if(defaultLoss < 0 || defaultLoss > 8) {
            logError("Default loss option in %s is out of range, resetting to %d...", registryName, DEFAULT_LOSS);
            defaultLoss = DEFAULT_LOSS;
        }
    }

    @Override
    protected void loadCustom(JsonObject entry) {
        String name = tryGetString(entry, "name", "");
        if(name.isEmpty()) {
            return;
        }
        ResourceLocation location = new ResourceLocation(name);
        String metadata = tryGetString(entry, "metadata", "0");
        int metadataVal = metadata.equals("*") ? OreDictionary.WILDCARD_VALUE : tryParseInt(metadata);

        String type = tryGetString(entry, "type", "");
        switch (type) {
            case "generate":
                String sourceName = tryGetString(entry, "sourceName", "");
                if (sourceName.isEmpty()) {
                    return;
                }
                ResourceLocation sourceLocation = new ResourceLocation(sourceName);
                Item sourceItem = Item.REGISTRY.getObject(sourceLocation);
                if(sourceItem == null) {
                    logUnknownItem(sourceLocation);
                    return;
                }
                String sourceMetadata = tryGetString(entry, "sourceMetadata", "0");
                int sourceMetadataVal = sourceMetadata.equals("*") ? OreDictionary.WILDCARD_VALUE : tryParseInt(sourceMetadata);
                ItemStack sourceStack = new ItemStack(sourceItem, 1, sourceMetadataVal);
                int sourceCount = tryGetInt(entry, "sourceCount", 9);

                if (location.getResourceDomain().equals("ore")) {
                    if (!addOreGenerated(location.getResourcePath(), sourceStack, sourceCount)) {
                        logUnknownOre(location);
                    }
                } else {
                    Item item = Item.REGISTRY.getObject(location);
                    if (item == null) {
                        logUnknownItem(location);
                        return;
                    }
                    addGeneratedEntry(new ItemStack(item, 1, metadataVal), sourceStack, sourceCount);
                }
                break;
            case "list":
                JsonArray rewards = tryGetArray(entry, "rewards");
                List<HeavySieveReward> rewardList = Lists.newArrayListWithCapacity(rewards.size());
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
                        int rewardMetadata = tryGetInt(reward, "metadata", 0);
                        float chance = tryGetFloat(reward, "chance", 1f);
                        if(chance > 1f) {
                            logError("Reward chance is out of range for %s in %s, capping at 1.0...", rewardLocation, registryName);
                            chance = 1f;
                        }
                        float luckMultiplier = tryGetFloat(reward, "luck", 0f);
                        int meshLevel = tryGetInt(reward, "meshLevel", 1);
                        rewardList.add(new HeavySieveReward(new ItemStack(item, count, rewardMetadata), chance, luckMultiplier, meshLevel));
                    } else {
                        logError("Failed to load %s registry; rewards must be an array of json objects", registryName);
                        return;
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
                        ItemStack itemStack = new ItemStack(item, 1, metadataVal);
                        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
                        if(state != null) {
                            HeavySieveRegistryEntry newEntry = new HeavySieveRegistryEntry(state, itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE);
                            for(HeavySieveReward reward : rewardList) {
                                newEntry.addReward(reward);
                            }
                            add(newEntry);
                        } else {
                            logError("Entry %s could not be registered for %s; it's not a block", location, registryName);
                        }
                    }
                }
                break;
            default:
                logError("Unknown type %s for %s in %s, skipping...", type, location, registryName);
                break;
        }
    }

    @Override
    protected void registerDefaults(JsonObject defaults) {
        final ItemStack GRAVEL = new ItemStack(Blocks.GRAVEL);
        final ItemStack SAND = new ItemStack(Blocks.SAND);
        final ItemStack DIRT = new ItemStack(Blocks.DIRT);
        final ItemStack SOUL_SAND = new ItemStack(Blocks.SOUL_SAND);
        final int COMPRESSION_SIZE = 9;
        if(tryGetBoolean(defaults, "excompressum:compressed_gravel", true)) {
            ItemStack itemStack = new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.GRAVEL.ordinal());
            addGeneratedEntry(itemStack, GRAVEL, COMPRESSION_SIZE - defaultLoss);
        }

        if(tryGetBoolean(defaults, "excompressum:compressed_sand", true)) {
            ItemStack itemStack = new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SAND.ordinal());
            addGeneratedEntry(itemStack, SAND, COMPRESSION_SIZE - defaultLoss);
        }

        if(tryGetBoolean(defaults, "excompressum:compressed_dirt", true)) {
            ItemStack itemStack = new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DIRT.ordinal());
            addGeneratedEntry(itemStack, DIRT, COMPRESSION_SIZE - defaultLoss);
        }

        if(tryGetBoolean(defaults, "excompressum:compressed_soul_sand", true)) {
            ItemStack itemStack = new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.SOUL_SAND.ordinal());
            addGeneratedEntry(itemStack, SOUL_SAND, COMPRESSION_SIZE - defaultLoss);
        }

        if(tryGetBoolean(defaults, "excompressum:compressed_dust", true)) {
            ItemStack dustBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.DUST);
            if(dustBlock != null) {
                ItemStack itemStack = new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.DUST.ordinal());
                addGeneratedEntry(itemStack, dustBlock, COMPRESSION_SIZE - defaultLoss);

            }
        }

        if(tryGetBoolean(defaults, "excompressum:compressed_nether_gravel", true)) {
            ItemStack netherGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.NETHER_GRAVEL);
            if(netherGravelBlock != null) {
                ItemStack itemStack = new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.NETHER_GRAVEL.ordinal());
                addGeneratedEntry(itemStack, netherGravelBlock, COMPRESSION_SIZE - defaultLoss);
            }
        }

        if(tryGetBoolean(defaults, "excompressum:compressed_ender_gravel", true)) {
            ItemStack enderGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.ENDER_GRAVEL);
            if(enderGravelBlock != null) {
                ItemStack itemStack = new ItemStack(ModBlocks.compressedBlock, 1, BlockCompressed.Type.ENDER_GRAVEL.ordinal());
                addGeneratedEntry(itemStack, enderGravelBlock, COMPRESSION_SIZE - defaultLoss);
            }
        }

        if (Loader.isModLoaded(Compat.EXTRAUTILS2)) {
            if (tryGetBoolean(defaults, "ExtraUtils2:CompressedGravel", true)) {
                ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "CompressedGravel");
                Item exUtilsBlock = Item.REGISTRY.getObject(location);
                if (exUtilsBlock != null) {
                    ItemStack itemStack = new ItemStack(exUtilsBlock, 1, 0);
                    addGeneratedEntry(itemStack, GRAVEL, COMPRESSION_SIZE - defaultLoss);
                }
            }

            if (tryGetBoolean(defaults, "ExtraUtils2:CompressedSand", true)) {
                ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "CompressedSand");
                Item exUtilsBlock = Item.REGISTRY.getObject(location);
                if (exUtilsBlock != null) {
                    ItemStack itemStack = new ItemStack(exUtilsBlock, 1, 0);
                    addGeneratedEntry(itemStack, SAND, COMPRESSION_SIZE - defaultLoss);
                }
            }

            if (tryGetBoolean(defaults, "ExtraUtils2:CompressedDirt", true)) {
                ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "CompressedDirt");
                Item exUtilsBlock = Item.REGISTRY.getObject(location);
                if (exUtilsBlock != null) {
                    ItemStack itemStack = new ItemStack(exUtilsBlock, 1, 0);
                    addGeneratedEntry(itemStack, DIRT, COMPRESSION_SIZE - defaultLoss);
                }
            }
        }
    }

    private boolean addOre(String oreName, List<HeavySieveReward> rewards) {
        List<ItemStack> list = OreDictionary.getOres(oreName, false);
        for(ItemStack itemStack : list) {
            IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
            if(state != null) {
                HeavySieveRegistryEntry entry = new HeavySieveRegistryEntry(state, itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE);
                for(HeavySieveReward reward : rewards) {
                    entry.addReward(reward);
                }
                add(entry);
            } else {
                ExCompressum.logger.warn("Ore dictionary entry {} in {} could not be registered for {}; it's not a block", itemStack.getItem().getRegistryName(), oreName, registryName);
            }
        }
        return list.size() > 0;
    }

    private boolean addOreGenerated(String oreName, ItemStack sourceStack, int sourceCount) {
        List<ItemStack> list = OreDictionary.getOres(oreName, false);
        for(ItemStack itemStack : list) {
            addGeneratedEntry(itemStack, sourceStack, sourceCount);
        }
        return list.size() > 0;
    }

    private void addGeneratedEntry(ItemStack itemStack, ItemStack sourceStack, int count) {
        IBlockState state = StupidUtils.getStateFromItemStack(itemStack);
        if(state == null) {
            logError("Entry %s could not be generated from %s for %s; it's not a block", itemStack.getItem().getRegistryName(), sourceStack.getItem().getRegistryName(), registryName);
            return;
        }
        HeavySieveRegistryEntry entry = new HeavySieveRegistryEntry(state, itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE);
        Collection<HeavySieveReward> rewards = ExRegistro.generateHeavyRewards(sourceStack, count);
        if(rewards.isEmpty()) {
            logError("Entry %s could not be generated in %s because %s is not an Ex Nihilo siftable", itemStack.getItem().getRegistryName(), registryName, sourceStack.getItem().getRegistryName());
            return;
        }
        entry.addRewards(rewards);
        add(entry);
    }

}
