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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import javax.annotation.Nullable;
import java.util.*;

public class LootTableUtils {

    private static final LootContextParam<ItemStack> SOURCE_STACK = new LootContextParam<>(new ResourceLocation(ExCompressum.MOD_ID, "source_stack"));

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

        LootTables lootTableManager = ExCompressum.proxy.getLootTableManager();
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
            List<LootPoolEntryContainer> entries = ((LootPoolAccessor) pool).getEntries();
            for (LootPoolEntryContainer entry : entries) {
                float entryBaseChance = getBaseChance(entry);
                float baseChance = entryBaseChance > 0 ? entryBaseChance : poolBaseChance;
                NumberProvider countRange = getCountRange(entry);
                if (entry instanceof LootItem) {
                    ItemStack itemStack = new ItemStack(((LootItem) entry).item);
                    itemStack.setCount(getMaxCount(countRange));
                    result.add(new LootTableEntry(itemStack, countRange, baseChance));
                } else if (entry instanceof TagEntry tagEntry) {
                    Tag<Item> tag = tagEntry.tag;
                    List<Item> items = tag.getValues();
                    for (Item item : items) {
                        ItemStack itemStack = new ItemStack(item);
                        itemStack.setCount(getMaxCount(countRange));
                        result.add(new LootTableEntry(itemStack, countRange, baseChance));
                    }
                } else if (entry instanceof NihiloLootEntry nihiloLootEntry) {
                    ExNihiloProvider.NihiloItems nihiloItem = nihiloLootEntry.getNihiloItem();
                    ItemStack itemStack = ExNihilo.getInstance().getNihiloItem(nihiloItem);
                    itemStack.setCount(getMaxCount(countRange));
                    result.add(new LootTableEntry(itemStack, countRange, baseChance));
                } else if (entry instanceof LootTableReference tableLootEntry) {
                    ResourceLocation lootTableLocation = tableLootEntry.table;
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

    private static float getBaseChance(LootPoolEntryContainer entry) {
        return getBaseChance(Arrays.asList(entry.conditions.clone()));
    }

    private static float getBaseChance(List<LootItemCondition> conditions) {
        for (LootItemCondition condition : conditions) {
            if (condition instanceof LootItemRandomChanceCondition) {
                return ((LootItemRandomChanceCondition) condition).chance;
            }
        }
        return 1f;
    }

    private static NumberProvider getCountRange(LootPoolEntryContainer entry) {
        if (entry instanceof LootPoolSingletonContainer) {
            for (LootItemFunction function : ((LootPoolSingletonContainer) entry).functions) {
                if (function instanceof SetItemCountFunction) {
                    return ((SetItemCountFunction) function).countRange;
                }
            }
        }

        return ConstantValue.exactly(1);
    }

    private static int getMaxCount(NumberProvider range) {
        if (range instanceof UniformGenerator uniform) {
            return (int) uniform.getMax();
        } else if (range instanceof BinomialDistributionGenerator binomial) {
            return (int) (binomial.n * binomial.p);
        } else if (range instanceof ConstantValue constant) {
            return (int) constant.value;
        }

        return 1;
    }

    public static LootContext buildLootContext(ServerLevel world, ItemStack itemStack, Random random) {
        return new LootContext.Builder(world)
                .withRandom(random)
                .withParameter(SOURCE_STACK, itemStack)
                .create(new LootContextParamSet.Builder().required(SOURCE_STACK).build());
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

    public static LootPoolSingletonContainer.Builder<?> buildLootEntry(ItemStack outputItem, float chance) {
        LootPoolSingletonContainer.Builder<?> entryBuilder = LootItem.lootTableItem(outputItem.getItem());
        if (outputItem.getCount() > 0) {
            entryBuilder.apply(SetItemCountFunction.setCount(ConstantValue.exactly(outputItem.getCount())));
        }
        if (outputItem.getTag() != null) {
            entryBuilder.apply(SetNbtFunction.setTag(outputItem.getTag()));
        }
        if (chance != -1f) {
            entryBuilder.when(LootItemRandomChanceCondition.randomChance(chance));
        }
        return entryBuilder;
    }
}
