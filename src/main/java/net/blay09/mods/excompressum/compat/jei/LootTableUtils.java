package net.blay09.mods.excompressum.compat.jei;

import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.loot.NihiloLootEntry;
import net.blay09.mods.excompressum.mixin.LootPoolAccessor;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.api.LootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.ITag;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.*;

public class LootTableUtils {

    private static final Random random = new Random();

    public static boolean isLootTableEmpty(@Nullable LootTableProvider lootTableProvider) {
        if (lootTableProvider == null) {
            return true;
        }

        return getLootTableEntries(lootTableProvider).isEmpty();
    }

    public static List<LootTableEntry> getLootTableEntries(@Nullable LootTableProvider lootTableProvider) {
        if (lootTableProvider == null) {
            return Collections.emptyList();
        }

        LootTableManager lootTableManager = ServerLifecycleHooks.getCurrentServer().getLootTableManager();
        LootTable lootTable = lootTableProvider.getLootTable("", lootTableManager);
        List<LootTableEntry> result = new ArrayList<>();
        List<LootPool> pools = ObfuscationReflectionHelper.getPrivateValue(LootTable.class, lootTable, "field_186466_c");
        for (LootPool pool : pools) {
            float poolBaseChance = getBaseChance(pool);
            List<LootEntry> entries = ((LootPoolAccessor) pool).getLootEntries();
            for (LootEntry entry : entries) {
                float entryBaseChance = getBaseChance(entry);
                float baseChance = entryBaseChance > 0 ? entryBaseChance : poolBaseChance;
                IRandomRange countRange = getCountRange(entry);
                if (entry instanceof ItemLootEntry) {
                    ItemStack itemStack = new ItemStack(((ItemLootEntry) entry).item);
                    itemStack.setCount(getMaxCount(countRange));
                    result.add(new LootTableEntry(itemStack, countRange, baseChance));
                } else if (entry instanceof TagLootEntry) {
                    ITag<Item> tag = ((TagLootEntry) entry).tag;
                    List<Item> items = tag.getAllElements();
                    for (Item item : items) {
                        ItemStack itemStack = new ItemStack(item);
                        itemStack.setCount(getMaxCount(countRange));
                        result.add(new LootTableEntry(itemStack, countRange, baseChance));
                    }
                } else if (entry instanceof NihiloLootEntry) {
                    ExNihiloProvider.NihiloItems nihiloItem = ((NihiloLootEntry) entry).getNihiloItem();
                    ItemStack itemStack = ExNihilo.getInstance().getNihiloItem(nihiloItem);
                    itemStack.setCount(getMaxCount(countRange));
                    result.add(new LootTableEntry(itemStack, countRange, baseChance));
                } else if (entry instanceof TableLootEntry) {
                    LootTableProvider provider = new LootTableProvider(((TableLootEntry) entry).table);
                    result.addAll(getLootTableEntries(provider));
                }
            }
        }
        return result;
    }

    private static float getBaseChance(LootPool pool) {
        return getBaseChance(((LootPoolAccessor) pool).getConditions());
    }

    private static float getBaseChance(LootEntry entry) {
        return getBaseChance(Arrays.asList(entry.conditions.clone()));
    }

    private static float getBaseChance(List<ILootCondition> conditions) {
        for (ILootCondition condition : conditions) {
            if (condition instanceof RandomChance) {
                return ((RandomChance) condition).chance;
            }
        }
        return 0f;
    }

    private static IRandomRange getCountRange(LootEntry entry) {
        if (entry instanceof StandaloneLootEntry) {
            for (ILootFunction function : ((StandaloneLootEntry) entry).functions) {
                if (function instanceof SetCount) {
                    return ((SetCount) function).countRange;
                }
            }
        }

        return ConstantRange.of(1);
    }

    private static int getMaxCount(IRandomRange range) {
        if (range instanceof RandomValueRange) {
            return (int) ((RandomValueRange) range).getMax();
        } else if (range instanceof BinomialRange) {
            return (int) (((BinomialRange) range).n * ((BinomialRange) range).p);
        } else if (range instanceof ConstantRange) {
            return range.generateInt(random);
        }

        return 1;
    }

}
