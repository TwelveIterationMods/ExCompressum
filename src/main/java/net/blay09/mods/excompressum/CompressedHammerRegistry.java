package net.blay09.mods.excompressum;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import exnihilo.registries.helpers.Smashable;
import exnihilo.utils.ItemInfo;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Collection;

public class CompressedHammerRegistry {

    private static Multimap<ItemInfo, Smashable> rewards = ArrayListMultimap.create();

    public static void register(Block source, int sourceMeta, Item output, int outputMeta, float chance, float luckMultiplier) {
        Smashable entry = new Smashable(source, sourceMeta, output, outputMeta, chance, luckMultiplier);
        ItemInfo itemInfo = new ItemInfo(source, sourceMeta);
        rewards.put(itemInfo, entry);
    }

    public static Collection<Smashable> getRewards(Block block, int meta) {
        return rewards.get(new ItemInfo(block, meta));
    }

    public static boolean registered(Block block, int meta) {
        return rewards.containsKey(new ItemInfo(block, meta));
    }

    public static void register(Block source, int sourceMeta, ItemStack reward) {
        for(int i = 0; i < reward.stackSize; i++) {
            register(source, sourceMeta, reward.getItem(), reward.getItemDamage(), 1f, 0f);
        }
    }

}
