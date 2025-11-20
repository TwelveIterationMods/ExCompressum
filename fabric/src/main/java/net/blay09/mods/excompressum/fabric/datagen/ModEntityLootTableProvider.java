package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.component.ModComponents;
import net.blay09.mods.excompressum.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class ModEntityLootTableProvider extends SimpleFabricLootTableProvider {

    private final CompletableFuture<HolderLookup.Provider> providerFuture;

    public ModEntityLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(dataOutput, provider, LootContextParamSets.ENTITY);
        this.providerFuture = provider;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> builder) {
        final var provider = providerFuture.resultNow();
        builder.accept(ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "entities/angry_chicken")),
                LootTable.lootTable()
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(ModItems.chickenStick)
                                        .apply(SetComponentsFunction.setComponent(ModComponents.angry.get(), Unit.INSTANCE)))
                        )
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.FEATHER)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider, UniformGenerator.between(0, 1)))
                                ))
                        .withPool(LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1))
                                .add(LootItem.lootTableItem(Items.CHICKEN)
                                        .apply(SmeltItemFunction.smelted()
                                                .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                                        EntityPredicate.Builder.entity().flags(EntityFlagsPredicate.Builder.flags().setOnFire(true)))))
                                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider, UniformGenerator.between(0, 1))))
                        )
        );
    }

}
