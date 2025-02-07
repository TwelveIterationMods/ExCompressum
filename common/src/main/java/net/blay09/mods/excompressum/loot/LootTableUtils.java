package net.blay09.mods.excompressum.loot;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.CommonLootTableAccessor;
import net.blay09.mods.excompressum.mixin.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import org.jetbrains.annotations.Nullable;
import java.util.*;

public class LootTableUtils {

    private static final LootContextParam<ItemStack> SOURCE_STACK = new LootContextParam<>(new ResourceLocation(ExCompressum.MOD_ID, "source_stack"));

    public static boolean isLootTableEmpty(@Nullable LootTable lootTable) {
        if (lootTable == null) {
            return true;
        }

        return getLootTableEntries(lootTable).isEmpty();
    }

    public static List<LootTableEntry> getLootTableEntries(@Nullable LootTable lootTable) {
        if (lootTable == null) {
            return Collections.emptyList();
        }

        List<LootTableEntry> result = new ArrayList<>();
        LootPool[] pools = ((CommonLootTableAccessor) lootTable).balm_getPools();
        for (LootPool pool : pools) {
            float poolBaseChance = getBaseChance(pool);
            LootPoolEntryContainer[] entries = ((LootPoolAccessor) pool).getEntries();
            for (LootPoolEntryContainer entry : entries) {
                float entryBaseChance = getBaseChance(entry);
                float baseChance = entryBaseChance > 0 ? entryBaseChance : poolBaseChance;
                NumberProvider countRange = getCountRange(entry);
                if (entry instanceof LootItemAccessor lootItem) {
                    ItemStack itemStack = new ItemStack(lootItem.getItem());
                    itemStack.setCount(Math.max(1, (int) getMaxCount(countRange)));
                    result.add(new LootTableEntry(itemStack, countRange, baseChance));
                } else if (entry instanceof TagEntryAccessor tagEntry) {
                    TagKey<Item> tag = tagEntry.getTag();
                    BuiltInRegistries.ITEM.getOrCreateTag(tag).forEach(itemHolder -> {
                        ItemStack itemStack = new ItemStack(itemHolder.value());
                        itemStack.setCount(Math.max(1, (int) getMaxCount(countRange)));
                        result.add(new LootTableEntry(itemStack, countRange, baseChance));
                    });
                }
            }
        }
        return result;
    }

    private static float getBaseChance(LootPool pool) {
        return getBaseChance(((LootPoolAccessor) pool).getConditions());
    }

    private static float getBaseChance(LootPoolEntryContainer entry) {
        return getBaseChance(((LootPoolEntryContainerAccessor) entry).getConditions().clone());
    }

    private static float getBaseChance(LootItemCondition[] conditions) {
        for (LootItemCondition condition : conditions) {
            if (condition instanceof LootItemRandomChanceConditionAccessor chanceCondition) {
                return chanceCondition.getProbability();
            }
        }
        return 1f;
    }

    private static NumberProvider getCountRange(LootPoolEntryContainer entry) {
        if (entry instanceof LootPoolSingletonContainerAccessor lootPoolSingletonContainer) {
            for (LootItemFunction function : lootPoolSingletonContainer.getFunctions()) {
                if (function instanceof SetItemCountFunctionAccessor setItemCountFunction) {
                    return setItemCountFunction.getValue();
                }
            }
        }

        return ConstantValue.exactly(1);
    }

    public static float getMinCount(NumberProvider range) {
        if (range instanceof UniformGeneratorAccessor uniform) {
            return getMinCount(uniform.getMin());
        } else if (range instanceof BinomialDistributionGeneratorAccessor binomial) {
            return getMinCount(binomial.getN()) * getMaxCount(binomial.getP());
        } else if (range instanceof ConstantValueAccessor constant) {
            return constant.getValue();
        }

        return 1;
    }

    public static float getMaxCount(NumberProvider range) {
        if (range instanceof UniformGeneratorAccessor uniform) {
            return getMaxCount(uniform.getMax());
        } else if (range instanceof BinomialDistributionGeneratorAccessor binomial) {
            return getMaxCount(binomial.getN()) * getMaxCount(binomial.getP());
        } else if (range instanceof ConstantValueAccessor constant) {
            return constant.getValue();
        }

        return 1;
    }

    public static LootContext buildLootContext(ServerLevel level, ItemStack itemStack) {
        final var params = new HashMap<LootContextParam<?>, Object>();
        params.put(SOURCE_STACK, itemStack);
        return new LootContext.Builder(new LootParams(level, params, Collections.emptyMap(), 0f)).create(null);
    }

    public static List<MergedLootTableEntry> mergeLootTableEntries(List<LootTableEntry> entries) {
        List<MergedLootTableEntry> result = new ArrayList<>();
        ArrayListMultimap<ResourceLocation, LootTableEntry> entryMap = ArrayListMultimap.create();
        for (LootTableEntry entry : entries) {
            if (entry.getItemStack().hasTag()) {
                result.add(new MergedLootTableEntry(entry));
            } else {
                final var itemId = Balm.getRegistries().getKey(entry.getItemStack().getItem());
                entryMap.put(itemId, entry);
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

    public static LootPoolSingletonContainer.Builder<?> buildLootEntry(ItemStack itemStack, NumberProvider amount) {
        LootPoolSingletonContainer.Builder<?> entryBuilder = LootItem.lootTableItem(itemStack.getItem());
        if (itemStack.getCount() > 0) {
            entryBuilder.apply(SetItemCountFunction.setCount(amount));
        }
        if (itemStack.getTag() != null) {
            entryBuilder.apply(SetNbtFunction.setTag(itemStack.getTag()));
        }
        return entryBuilder;
    }
}
