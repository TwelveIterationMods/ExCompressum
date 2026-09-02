package net.blay09.mods.excompressum.loot;

import com.google.common.collect.ArrayListMultimap;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.mixin.*;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKey;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ints.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.UniformGenerator;

import org.jspecify.annotations.Nullable;

import java.util.*;

public class LootTableUtils {

    private static final ContextKey<ItemInstance> SOURCE_STACK = new ContextKey<>(Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID,
            "source_stack"));

    private static final ContextKeySet CONTEXT_KEY_SET = new ContextKeySet.Builder().required(SOURCE_STACK).build();

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
        final var pools = ((LootTableAccessor) lootTable).getPools();
        for (final var pool : pools) {
            final var poolBaseChance = getBaseChance(((LootPoolAccessor) pool).getCondition());
            final var entries = ((LootPoolAccessor) pool).getEntries();
            for (LootPoolEntryContainer entry : entries) {
                final var entryBaseChance = getBaseChance(((LootPoolEntryContainerAccessor) entry).getCondition());
                final var baseChance = entryBaseChance.orElse(poolBaseChance.orElseGet(() -> new net.minecraft.world.level.storage.loot.providers.number.floats.ConstantValue(1f)));
                ContextIntProvider countRange = getCountRange(entry);
                if (entry instanceof LootItemAccessor lootItem) {
                    ItemStack itemStack = new ItemStack(lootItem.getItem());
                    itemStack.setCount(Math.max(1, (int) getMaxCount(countRange)));
                    result.add(new LootTableEntry(itemStack, countRange, baseChance));
                } else if (entry instanceof TagEntryAccessor tagEntry) {
                    tagEntry.getTag().forEach(itemHolder -> {
                        ItemStack itemStack = new ItemStack(itemHolder.value());
                        itemStack.setCount(Math.max(1, (int) getMaxCount(countRange)));
                        result.add(new LootTableEntry(itemStack, countRange, baseChance));
                    });
                }
            }
        }
        return result;
    }

    private static Optional<ContextFloatProvider> getBaseChance(Optional<Holder<LootItemCondition>> condition) {
        if (condition.isPresent() && condition.get().value() instanceof LootItemRandomChanceCondition chanceCondition) {
            return Optional.of(chanceCondition.chance().value());
        }

        return Optional.empty();
    }

    private static ContextIntProvider getCountRange(LootPoolEntryContainer entry) {
        final var modifier = ((LootPoolEntryContainerAccessor) entry).getModifier();
        if (modifier.isPresent() && modifier.get().value() instanceof SetItemCountFunctionAccessor setItemCountFunction) {
            return setItemCountFunction.getCount().value();
        }

        return new ConstantValue(1);
    }

    public static float getMinCount(ContextIntProvider range) {
        return switch (range) {
            case UniformGenerator uniform -> getMinCount(uniform.min().value());
            case BinomialDistributionGenerator binomial -> getMinCount(binomial.n().value()) * getMaxValue(binomial.p().value());
            case ConstantValue constant -> constant.value();
            default -> 1;
        };

    }

    public static float getMaxCount(ContextIntProvider range) {
        return switch (range) {
            case UniformGenerator uniform -> getMaxCount(uniform.max().value());
            case BinomialDistributionGenerator binomial -> getMaxCount(binomial.n().value()) * getMaxValue(binomial.p().value());
            case ConstantValue constant -> constant.value();
            default -> 1;
        };

    }

    public static LootContext buildLootContext(ServerLevel level, ItemInstance itemStack) {
        final var params = ContextMap.builder().set(SOURCE_STACK, itemStack);
        return new LootContext.Builder(new LootParams(level, params.buildAndValidate(CONTEXT_KEY_SET), Collections.emptyMap(), 0f)).create(Optional.empty());
    }

    public static List<MergedLootTableEntry> mergeLootTableEntries(List<LootTableEntry> entries) {
        List<MergedLootTableEntry> result = new ArrayList<>();
        ArrayListMultimap<Identifier, LootTableEntry> entryMap = ArrayListMultimap.create();
        for (LootTableEntry entry : entries) {
            if (!entry.itemStack().getComponents().isEmpty()) {
                result.add(new MergedLootTableEntry(entry));
            } else {
                final var itemId = BuiltInRegistries.ITEM.getKey(entry.itemStack().getItem());
                entryMap.put(itemId, entry);
            }
        }

        for (Identifier key : entryMap.keySet()) {
            List<LootTableEntry> mergableEntries = entryMap.get(key);
            LootTableEntry firstEntry = mergableEntries.getFirst();
            // TODO mergableEntries.sort(Comparator.comparing(LootTableEntry::getBaseChance).reversed());
            result.add(new MergedLootTableEntry(firstEntry.itemStack(), mergableEntries));
        }
        return result;
    }

    public static UniformContainerBase.Builder<?> buildLootEntry(ItemStack outputItem, float chance) {
        UniformContainerBase.Builder<?> entryBuilder = LootItem.lootTableItem(outputItem.getItem());
        if (outputItem.getCount() > 0) {
            entryBuilder.apply(SetItemCountFunction.setCount(Holder.direct(new ConstantValue(outputItem.getCount()))));
        }
        for (final var component : outputItem.getComponents()) {
            entryBuilder.apply(copyComponent(component));
        }
        if (chance != -1f) {
            entryBuilder.when(LootItemRandomChanceCondition.randomChance(chance));
        }
        return entryBuilder;
    }

    public static UniformContainerBase.Builder<?> buildLootEntry(ItemStack itemStack, ContextIntProvider amount) {
        UniformContainerBase.Builder<?> entryBuilder = LootItem.lootTableItem(itemStack.getItem());
        if (itemStack.getCount() > 0) {
            entryBuilder.apply(SetItemCountFunction.setCount(Holder.direct(amount)));
        }
        for (final var component : itemStack.getComponents()) {
            entryBuilder.apply(copyComponent(component));
        }
        return entryBuilder;
    }

    private static float getMaxValue(ContextFloatProvider provider) {
        return provider instanceof net.minecraft.world.level.storage.loot.providers.number.floats.ConstantValue(
                float value
        ) ? value : 1f;
    }

    private static <T> LootItemConditionalFunction.Builder<?> copyComponent(TypedDataComponent<T> component) {
        return SetComponentsFunction.setComponent(component.type(), component.value());
    }
}
