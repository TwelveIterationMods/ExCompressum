package net.blay09.mods.excompressum.registry.compressedhammer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.ReloadRegistryEvent;
import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerRegistryEntry;
import net.blay09.mods.excompressum.api.compressedhammer.CompressedHammerReward;
import net.blay09.mods.excompressum.block.CompressedBlockType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.registry.AbstractRegistry;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;

/*public class CompressedHammerRegistryOld extends AbstractRegistry {

    public static final CompressedHammerRegistryOld INSTANCE = new CompressedHammerRegistryOld();
    private Map<ResourceLocation, CompressedHammerRegistryEntry> entries = Maps.newHashMap();

    public CompressedHammerRegistryOld() {
        super("CompressedHammer");
    }

    public Map<ResourceLocation, CompressedHammerRegistryEntry> getEntries() {
        return entries;
    }

    public void add(CompressedHammerRegistryEntry entry) {
        final ResourceLocation registryName = entry.getInputState().getBlock().getRegistryName();
        CompressedHammerRegistryEntry previousEntry = entries.get(registryName);
        if (previousEntry != null) {
            for (CompressedHammerReward reward : entry.getRewards()) {
                previousEntry.addReward(reward);
            }
        } else {
            entries.put(registryName, entry);
        }
    }

    @Nullable
    public static CompressedHammerRegistryEntry getEntryForBlockState(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return INSTANCE.entries.get(registryName);
    }

    public static boolean isHammerable(BlockState state) {
        return getEntryForBlockState(state) != null;
    }

    public static boolean isHammerable(ItemStack itemStack) {
        BlockState state = StupidUtils.getStateFromItemStack(itemStack);
        return state != null && isHammerable(state);
    }

    public static Collection<ItemStack> rollHammerRewards(BlockState state, float luck, Random rand) {
        CompressedHammerRegistryEntry entry = getEntryForBlockState(state);
        if (entry != null) {
            List<ItemStack> list = Lists.newArrayList();
            for (CompressedHammerReward reward : entry.getRewards()) {
                float chance = reward.getBaseChance() + reward.getLuckMultiplier() * luck;
                if (rand.nextFloat() < chance) {
                    list.add(reward.getItemStack().copy());
                }
            }
            return list;
        }
        return Collections.emptyList();
    }

    public static Collection<ItemStack> rollHammerRewards(ItemStack itemStack, float luck, Random rand) {
        BlockState state = StupidUtils.getStateFromItemStack(itemStack);
        if (state != null) {
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
        if (name.isEmpty()) {
            return;
        }

        ResourceLocation location = new ResourceLocation(name);
        JsonArray rewards = tryGetArray(entry, "rewards");
        List<CompressedHammerReward> rewardList = Lists.newArrayListWithCapacity(rewards.size());
        for (int i = 0; i < rewards.size(); i++) {
            JsonElement element = rewards.get(i);
            if (element.isJsonObject()) {
                JsonObject reward = element.getAsJsonObject();
                String rewardName = tryGetString(reward, "name", "");
                if (rewardName.isEmpty()) {
                    continue;
                }

                ResourceLocation rewardLocation = new ResourceLocation(rewardName);
                Item item = ForgeRegistries.ITEMS.getValue(rewardLocation);
                if (item == null) {
                    logUnknownItem(rewardLocation);
                    continue;
                }

                int count = tryGetInt(reward, "count", 1);
                float chance = tryGetFloat(reward, "chance", 1f);
                if (chance > 1f) {
                    logError("Reward chance is out of range for %s in %s, capping at 1.0...", rewardLocation, registryName);
                    chance = 1f;
                }
                float luckMultiplier = tryGetFloat(reward, "luck", 0f);
                rewardList.add(new CompressedHammerReward(new ItemStack(item, count), chance, luckMultiplier));
            } else {
                logError("Failed to preInit %s registry: rewards must be an array of json objects in ", registryName);
                return;
            }
        }
        if (location.getNamespace().equals("ore")) {
            if (!addOre(location.getPath(), rewardList)) {
                logUnknownOre(location);
            }
        } else {
            Item item = ForgeRegistries.ITEMS.getValue(location);
            if (item == null) {
                logUnknownItem(location);
                return;
            }

            ItemStack itemStack = new ItemStack(item);
            BlockState state = StupidUtils.getStateFromItemStack(itemStack);
            if (state != null) {
                CompressedHammerRegistryEntry newEntry = new CompressedHammerRegistryEntry(state);
                for (CompressedHammerReward reward : rewardList) {
                    newEntry.addReward(reward);
                }
                add(newEntry);
            } else {
                logError("Entry %s could not be registered for %s; it's not a block", location, registryName);
            }
        }
    }

    private boolean addOre(String oreName, List<CompressedHammerReward> rewards) {
        /*List<ItemStack> list = OreDictionary.getOres(oreName, false);
        for (ItemStack itemStack : list) {
            BlockState state = StupidUtils.getStateFromItemStack(itemStack);
            if (state != null) {
                CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(state);
                for (CompressedHammerReward reward : rewards) {
                    entry.addReward(reward);
                }
                add(entry);
            } else {
                ExCompressum.logger.warn("Ore dictionary entry {} in {} could not be registered for {}; it's not a block", itemStack.getItem().getRegistryName(), oreName, registryName);
            }
        }
        return list.size() > 0;
        return false;
    }

    @Override
    protected void registerDefaults(JsonObject defaults) {
        if (tryGetBoolean(defaults, "excompressum:compressed_cobblestone", true)) {
            CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(ModBlocks.compressedBlocks[CompressedBlockType.COBBLESTONE.ordinal()].getDefaultState());
            entry.addReward(new CompressedHammerReward(new ItemStack(Blocks.GRAVEL, 9), 1f, 0f));
            add(entry);
        }

        if (tryGetBoolean(defaults, "excompressum:compressed_gravel", true)) {
            CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(ModBlocks.compressedBlocks[CompressedBlockType.GRAVEL.ordinal()].getDefaultState());
            entry.addReward(new CompressedHammerReward(new ItemStack(Blocks.SAND, 9), 1f, 0f));
            add(entry);
        }

        if (tryGetBoolean(defaults, "excompressum:compressed_sand", true)) {
            ItemStack dustBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.DUST);
            if (!dustBlock.isEmpty()) {
                CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(ModBlocks.compressedBlocks[CompressedBlockType.SAND.ordinal()].getDefaultState());
                entry.addReward(new CompressedHammerReward(ItemHandlerHelper.copyStackWithSize(dustBlock, 9), 1f, 0f));
                add(entry);
            }
        }

        if (tryGetBoolean(defaults, "excompressum:compressed_netherrack", true)) {
            ItemStack netherGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.NETHER_GRAVEL);
            if (!netherGravelBlock.isEmpty()) {
                CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(ModBlocks.compressedBlocks[CompressedBlockType.NETHERRACK.ordinal()].getDefaultState());
                entry.addReward(new CompressedHammerReward(ItemHandlerHelper.copyStackWithSize(netherGravelBlock, 9), 1f, 0f));
                add(entry);
            }
        }

        if (tryGetBoolean(defaults, "excompressum:compressed_end_stone", true)) {
            ItemStack enderGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.ENDER_GRAVEL);
            if (!enderGravelBlock.isEmpty()) {
                CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(ModBlocks.compressedBlocks[CompressedBlockType.END_STONE.ordinal()].getDefaultState());
                entry.addReward(new CompressedHammerReward(ItemHandlerHelper.copyStackWithSize(enderGravelBlock, 9), 1f, 0f));
                add(entry);
            }
        }

        if (ModList.get().isLoaded(Compat.EXTRAUTILS2)) {
            if (tryGetBoolean(defaults, "ExtraUtils2:CompressedCobblestone", true)) {
                ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "compressedcobblestone");
                if (ForgeRegistries.BLOCKS.containsKey(location)) {
                    Block exUtilsBlock = ForgeRegistries.BLOCKS.getValue(location);
                    CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(exUtilsBlock.getDefaultState());
                    entry.addReward(new CompressedHammerReward(new ItemStack(Blocks.GRAVEL, 9), 1f, 0f));
                    add(entry);
                }
            }

            if (tryGetBoolean(defaults, "ExtraUtils2:CompressedGravel", true)) {
                ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "compressedgravel");
                if (ForgeRegistries.BLOCKS.containsKey(location)) {
                    Block exUtilsBlock = ForgeRegistries.BLOCKS.getValue(location);
                    CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(exUtilsBlock.getDefaultState());
                    entry.addReward(new CompressedHammerReward(new ItemStack(Blocks.SAND, 9), 1f, 0f));
                    add(entry);
                }
            }

            if (tryGetBoolean(defaults, "ExtraUtils2:CompressedSand", true)) {
                ItemStack dustBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.DUST);
                if (!dustBlock.isEmpty()) {
                    ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "compressedsand");
                    if (ForgeRegistries.BLOCKS.containsKey(location)) {
                        Block exUtilsBlock = ForgeRegistries.BLOCKS.getValue(location);
                        CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(exUtilsBlock.getDefaultState());
                        entry.addReward(new CompressedHammerReward(ItemHandlerHelper.copyStackWithSize(dustBlock, 9), 1f, 0f));
                        add(entry);
                    }
                }
            }

            if (tryGetBoolean(defaults, "ExtraUtils2:CompressedNetherrack", true)) {
                ItemStack netherGravelBlock = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.NETHER_GRAVEL);
                if (!netherGravelBlock.isEmpty()) {
                    ResourceLocation location = new ResourceLocation(Compat.EXTRAUTILS2, "compressednetherrack");
                    if (ForgeRegistries.BLOCKS.containsKey(location)) {
                        Block exUtilsBlock = ForgeRegistries.BLOCKS.getValue(location);
                        CompressedHammerRegistryEntry entry = new CompressedHammerRegistryEntry(exUtilsBlock.getDefaultState());
                        entry.addReward(new CompressedHammerReward(ItemHandlerHelper.copyStackWithSize(netherGravelBlock, 9), 1f, 0f));
                        add(entry);
                    }
                }
            }
        }
    }

    @Override
    protected ReloadRegistryEvent getRegistryEvent() {
        return new ReloadRegistryEvent.CompressedHammer();
    }

}
*/