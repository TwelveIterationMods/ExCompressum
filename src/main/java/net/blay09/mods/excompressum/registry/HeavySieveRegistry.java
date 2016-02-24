package net.blay09.mods.excompressum.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class HeavySieveRegistry {

    private static Multimap<ItemInfo, SiftingResult> siftables = ArrayListMultimap.create();
    private static Configuration config;

    public static void register(Block source, int sourceMeta, Item output, int outputMeta, int rarity) {
        if (source == null || output == null) {
            return;
        }
        if (rarity > 0) {
            siftables.put(new ItemInfo(source, sourceMeta), new SiftingResult(output, outputMeta, rarity));
        }
    }

    public static Collection<SiftingResult> getSiftingOutput(Block inBlock, int meta) {
        return siftables.get(new ItemInfo(inBlock, meta));
    }

    public static Collection<SiftingResult> getSiftingOutput(ItemStack itemStack) {
        return siftables.get(new ItemInfo(itemStack));
    }

    public static Collection<SiftingResult> getSiftingOutput(ItemInfo itemInfo) {
        return siftables.get(itemInfo);
    }

    public static boolean isRegistered(Block block, int metadata) {
        return siftables.containsKey(new ItemInfo(block, metadata));
    }

    public static void load(Configuration config) {
        HeavySieveRegistry.config = config;
        reload();
    }

    public static void reload() {
        siftables.clear();
        String[] generatedSiftables = config.getStringList("Generate Heavy Siftables", "registries", new String[]{
                "ExtraUtilities:cobblestone_compressed:8=minecraft:dirt:0:1:5",
                "ExtraUtilities:cobblestone_compressed:12=minecraft:gravel:0:1:5",
                "ExtraUtilities:cobblestone_compressed:14=minecraft:sand:0:1:5",
                "excompressum:compressed_dust=exnihilo:dust:0:1:5"
        }, "Here you can map normal siftables to heavy siftable blocks to automatically generate rewards for them based on ExNihilo's registry. Format: modid:name:meta=modid:name:meta:rarityMultiplier:amountMultiplier");
        for (String siftable : generatedSiftables) {
            String[] s = siftable.split("=");
            if (s.length < 2) {
                ExCompressum.logger.error("Skipping heavy siftable mapping " + siftable + " due to invalid format");
                continue;
            }
            String[] source = s[0].split(":");
            if (source[0].equals("ore") && source.length >= 2) {
                String oreName = source[1];
                List<ItemStack> ores = OreDictionary.getOres(oreName, false);
                if (!ores.isEmpty()) {
                    for (ItemStack ore : ores) {
                        if (ore.getItem() instanceof ItemBlock) {
                            mapSiftable(((ItemBlock) ore.getItem()).field_150939_a, ore.getItem().getMetadata(ore.getItemDamage()), s[1]);
                        } else {
                            ExCompressum.logger.error("Skipping heavy siftable mapping " + siftable + " because the source block is not a block");
                        }
                    }
                } else {
                    ExCompressum.logger.error("Skipping heavy siftable mapping " + siftable + " because no ore dictionary entries found");
                }
            } else {
                Block sourceBlock;
                if (source.length == 1) {
                    sourceBlock = GameRegistry.findBlock("minecraft", source[0]);
                } else {
                    sourceBlock = GameRegistry.findBlock(source[0], source[1]);
                }
                if (sourceBlock == null) {
                    ExCompressum.logger.error("Skipping heavy siftable mapping " + siftable + " because the source block was not found");
                }
                int sourceMeta = 0;
                if (source.length >= 3) {
                    if (source[2].equals("*")) {
                        sourceMeta = OreDictionary.WILDCARD_VALUE;
                    } else {
                        sourceMeta = Integer.parseInt(source[2]);
                    }
                }
                mapSiftable(sourceBlock, sourceMeta, s[1]);
            }
        }

        String[] siftables = config.getStringList("Heavy Siftables", "registries", new String[0], "Here you can add additional siftables for the heavy sieve. Format: modid:name:meta=modid:name:meta:rarity");
        for (String siftable : siftables) {
            String[] s = siftable.split("=");
            if (s.length < 2) {
                ExCompressum.logger.error("Skipping siftable " + siftable + " due to invalid format");
                continue;
            }
            String[] source = s[0].split(":");
            if (source[0].equals("ore") && source.length >= 2) {
                String oreName = source[1];
                List<ItemStack> ores = OreDictionary.getOres(oreName, false);
                if (!ores.isEmpty()) {
                    for (ItemStack ore : ores) {
                        if (ore.getItem() instanceof ItemBlock) {
                            loadSiftable(((ItemBlock) ore.getItem()).field_150939_a, ore.getItem().getMetadata(ore.getItemDamage()), s[1]);
                        } else {
                            ExCompressum.logger.error("Skipping siftable " + siftable + " because the source block is not a block");
                        }
                    }
                } else {
                    ExCompressum.logger.error("Skipping siftable " + siftable + " because no ore dictionary entries found");
                }
            } else {
                Block sourceBlock;
                if (source.length == 1) {
                    sourceBlock = GameRegistry.findBlock("minecraft", source[0]);
                } else {
                    sourceBlock = GameRegistry.findBlock(source[0], source[1]);
                }
                if (sourceBlock == null) {
                    ExCompressum.logger.error("Skipping siftable " + siftable + " because the source block was not found");
                    continue;
                }
                int sourceMeta = 0;
                if (source.length >= 3) {
                    if (source[2].equals("*")) {
                        sourceMeta = OreDictionary.WILDCARD_VALUE;
                    } else {
                        sourceMeta = Integer.parseInt(source[2]);
                    }
                }
                loadSiftable(sourceBlock, sourceMeta, s[1]);
            }
        }
    }

    private static void loadSiftable(Block sourceBlock, int sourceMeta, String reward) {
        String[] s = reward.split(":");
        if (s.length < 4) {
            ExCompressum.logger.error("Skipping siftable " + reward + " due to invalid format");
            return;
        }
        Item rewardItem = GameRegistry.findItem(s[0], s[1]);
        if (rewardItem == null) {
            ExCompressum.logger.error("Skipping siftable " + reward + " due to reward item not found");
            return;
        }
        int rewardMeta = Integer.parseInt(s[2]);
        register(sourceBlock, sourceMeta, rewardItem, rewardMeta, Integer.parseInt(s[3]));
    }

    private static void mapSiftable(Block sourceBlock, int sourceMeta, String mapping) {
        String[] s = mapping.split(":");
        if (s.length < 5) {
            ExCompressum.logger.error("Skipping heavy siftable mapping " + mapping + " due to invalid format");
            return;
        }
        Block mappedBlock = GameRegistry.findBlock(s[0], s[1]);
        if (mappedBlock == null) {
            ExCompressum.logger.error("Skipping heavy siftable mapping " + mapping + " due to mapped block not found");
            return;
        }
        int mappedMeta = Integer.parseInt(s[2]);
        List<SiftingResult> results = SieveRegistry.getSiftingOutput(mappedBlock, mappedMeta);
        if (results == null || results.isEmpty()) {
            ExCompressum.logger.error("Skipping heavy siftable mapping " + mapping + " due to not being registered in Ex Nihilo");
            return;
        }
        float rarityMultiplier = Float.parseFloat(s[3]);
        int amountMultiplier = Integer.parseInt(s[4]);
        for (SiftingResult result : results) {
            for (int i = 0; i < amountMultiplier; i++) {
                register(sourceBlock, sourceMeta, result.item, result.meta, Math.round(Math.max(1, result.rarity / rarityMultiplier)));
            }
        }
    }

    public static Multimap<ItemInfo, SiftingResult> getSiftables() {
        return siftables;
    }

    public static Collection<ItemInfo> getSources(ItemStack reward) {
        ArrayList<ItemInfo> result = new ArrayList<ItemInfo>();
        ItemInfo rewardInfo = new ItemInfo(reward);
        for(Map.Entry<ItemInfo, SiftingResult> entry : siftables.entries()) {
            if (new ItemInfo(entry.getValue().item, entry.getValue().meta).equals(rewardInfo)) {
                result.add(entry.getKey());
            }
        }
        return result;
    }
}
