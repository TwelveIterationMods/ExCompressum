package net.blay09.mods.excompressum.compat.jei;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.api.ILootTableProvider;
import net.blay09.mods.excompressum.loot.NihiloLootEntry;
import net.blay09.mods.excompressum.mixin.LootPoolAccessor;
import net.blay09.mods.excompressum.mixin.LootTableAccessor;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetNBT;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.*;

public class LootTableUtils {

    private static final LootParameter<ItemStack> SOURCE_STACK = new LootParameter<>(new ResourceLocation(ExCompressum.MOD_ID, "source_stack"));

    private static final Random random = new Random();

    public static boolean isLootTableEmpty(@Nullable LootTable lootTable) {
        if (lootTable == null) {
            return true;
        }

        return getLootTableEntries(lootTable).isEmpty();
    }

    public static List<LootTableEntry> getLootTableEntries(ResourceLocation id, @Nullable ILootTableProvider lootTableProvider) {
        if (lootTableProvider == null) {
            return Collections.emptyList();
        }

        LootTableManager lootTableManager = ExCompressum.proxy.getLootTableManager();
        LootTable lootTable = lootTableProvider.getLootTable(id, lootTableManager);
        return getLootTableEntries(lootTable);
    }

    public static List<LootTableEntry> getLootTableEntries(@Nullable LootTable lootTable) {
        if (lootTable == null) {
            return Collections.emptyList();
        }

        List<LootTableEntry> result = new ArrayList<>();
        List<LootPool> pools = ((LootTableAccessor) lootTable).getPools();
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
                    ResourceLocation lootTableLocation = ((TableLootEntry) entry).table;
                    LootTableProvider provider = new LootTableProvider(lootTableLocation);
                    result.addAll(getLootTableEntries(lootTableLocation, provider));
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
        return 1f;
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

    public static LootContext buildLootContext(ServerWorld world, ItemStack itemStack, Random random) {
        return new LootContext.Builder(world)
                .withRandom(random)
                .withParameter(SOURCE_STACK, itemStack)
                .build(new LootParameterSet.Builder().required(SOURCE_STACK).build());
    }

    public static List<MergedLootTableEntry> mergeLootTableEntries(List<LootTableEntry> entries) {
        List<MergedLootTableEntry> result = new ArrayList<>();
        ArrayListMultimap<ResourceLocation, LootTableEntry> entryMap = ArrayListMultimap.create();
        for (LootTableEntry entry : entries) {
            if (entry.getItemStack().hasTag()) {
                result.add(new MergedLootTableEntry(entry));
            } else {
                entryMap.put(entry.getItemStack().getItem().getRegistryName(), entry);
            }
        }

        for (ResourceLocation key : entryMap.keySet()) {
            List<LootTableEntry> mergableEntries = entryMap.get(key);
            LootTableEntry firstEntry = mergableEntries.get(0);
            mergableEntries.sort(Comparator.comparing(LootTableEntry::getBaseChance).reversed());
            result.add(new MergedLootTableEntry(firstEntry.getItemStack(), mergableEntries));
        }
        return result;
    }

    public static StandaloneLootEntry.Builder<?> buildLootEntry(ItemStack outputItem, float chance) {
        StandaloneLootEntry.Builder<?> entryBuilder = ItemLootEntry.builder(outputItem.getItem());
        if (outputItem.getCount() > 0) {
            entryBuilder.acceptFunction(SetCount.builder(ConstantRange.of(outputItem.getCount())));
        }
        if (outputItem.getTag() != null) {
            entryBuilder.acceptFunction(SetNBT.builder(outputItem.getTag()));
        }
        if (chance != -1f) {
            entryBuilder.acceptCondition(RandomChance.builder(chance));
        }
        return entryBuilder;
    }
}
