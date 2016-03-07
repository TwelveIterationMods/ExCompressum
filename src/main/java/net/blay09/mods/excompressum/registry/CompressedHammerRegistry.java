package net.blay09.mods.excompressum.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Smashable;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.data.ItemAndMetadata;
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

public class CompressedHammerRegistry {

    private static final Multimap<ItemAndMetadata, Smashable> smashables = ArrayListMultimap.create();

    private static void register(Block source, int sourceMeta, Item output, int outputMeta, float chance, float luckMultiplier) {
        Smashable entry = new Smashable(source, sourceMeta, output, outputMeta, chance, luckMultiplier);
        smashables.put(new ItemAndMetadata(source, sourceMeta), entry);
    }

    public static Collection<Smashable> getSmashables(Block block, int metadata) {
        return getSmashables(new ItemAndMetadata(block, metadata));
    }

    public static Collection<Smashable> getSmashables(ItemStack itemStack) {
        return getSmashables(new ItemAndMetadata(itemStack));
    }

    public static Collection<Smashable> getSmashables(ItemAndMetadata itemInfo) {
        return smashables.get(itemInfo);
    }

    public static boolean isRegistered(Block block, int metadata) {
        return smashables.containsKey(new ItemAndMetadata(block, metadata));
    }

    public static boolean isRegistered(ItemStack itemStack) {
        return smashables.containsKey(new ItemAndMetadata(itemStack));
    }

    public static Multimap<ItemAndMetadata, Smashable> getSmashables() {
        return smashables;
    }

    private static void register(Block source, int sourceMeta, ItemStack reward, float chance, float luckMultiplier) {
        for (int i = 0; i < reward.stackSize; i++) {
            register(source, sourceMeta, reward.getItem(), reward.getItemDamage(), chance, luckMultiplier);
        }
    }

    public static void load(Configuration config, boolean easyMode) {
        String[] smashables = config.getStringList("Smashables", "registries", new String[]{
                "ExtraUtilities:cobblestone_compressed:0=9:minecraft:gravel:0:1:0", "excompressum:compressed_dust:1=9:minecraft:gravel:0:1:0",
                "ExtraUtilities:cobblestone_compressed:12=9:minecraft:sand:0:1:0", "excompressum:compressed_dust:2=9:minecraft:sand:0:1:0",
                "ExtraUtilities:cobblestone_compressed:14=9:exnihilo:dust:0:1:0", "excompressum:compressed_dust:3=9:exnihilo:dust:0:1:0",
        }, "Here you can add additional smashables for the compressed hammers. Format: modid:name:meta=stackSize:modid:name:meta:chance:luckMultiplier");
        for (String smashable : smashables) {
            String[] s = smashable.split("=");
            if (s.length < 2) {
                ExCompressum.logger.error("Skipping smashable " + smashable + " due to invalid format");
                continue;
            }
            String[] source = s[0].split(":");
            if (source[0].equals("ore") && source.length >= 2) {
                String oreName = source[1];
                List<ItemStack> ores = OreDictionary.getOres(oreName, false);
                if (!ores.isEmpty()) {
                    for (ItemStack ore : ores) {
                        if (ore.getItem() instanceof ItemBlock) {
                            loadSmashable(((ItemBlock) ore.getItem()).field_150939_a, ore.getItem().getMetadata(ore.getItemDamage()), s[1], easyMode);
                        } else {
                            ExCompressum.logger.error("Skipping smashable " + smashable + " because the source block is not a block");
                        }
                    }
                } else {
                    ExCompressum.logger.error("Skipping smashable " + smashable + " because no ore dictionary entries found");
                }
            } else {
                Block sourceBlock;
                if (source.length == 1) {
                    sourceBlock = GameRegistry.findBlock("minecraft", source[0]);
                } else {
                    sourceBlock = GameRegistry.findBlock(source[0], source[1]);
                }
                if (sourceBlock == null) {
                    ExCompressum.logger.error("Skipping smashable " + smashable + " because the source block was not found");
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
                loadSmashable(sourceBlock, sourceMeta, s[1], easyMode);
            }
        }
    }

    private static void loadSmashable(Block sourceBlock, int sourceMeta, String reward, boolean easyMode) {
        String[] s = reward.split(":");
        if (s.length < 6) {
            ExCompressum.logger.error("Skipping smashable " + reward + " due to invalid format");
            return;
        }
        ItemStack rewardStack = GameRegistry.findItemStack(s[1], s[2], Integer.parseInt(s[0]));
        rewardStack.setItemDamage(Integer.parseInt(s[3]));
        if (!easyMode) {
            register(sourceBlock, sourceMeta, rewardStack, Float.parseFloat(s[4]), Float.parseFloat(s[5]));
        } else {
            for (int i = 0; i < rewardStack.stackSize; i++) {
                HammerRegistry.register(sourceBlock, sourceMeta, rewardStack.getItem(), rewardStack.getItemDamage(), Float.parseFloat(s[4]), Float.parseFloat(s[5]));
            }
        }
    }

    public static Collection<ItemAndMetadata> getSources(ItemStack reward) {
        ArrayList<ItemAndMetadata> results = new ArrayList<ItemAndMetadata>();
        for(Map.Entry<ItemAndMetadata, Smashable> entry : smashables.entries()) {
            if(entry.getValue().item == reward.getItem() && entry.getValue().meta == reward.getItemDamage()) {
                results.add(entry.getKey());
            }
        }
        return results;
    }
}
