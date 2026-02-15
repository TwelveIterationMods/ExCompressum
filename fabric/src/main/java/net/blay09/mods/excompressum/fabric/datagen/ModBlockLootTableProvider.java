package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.block.ModBlockStateProperties;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.concurrent.CompletableFuture;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected ModBlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> provider) {
        super(dataOutput, provider);
    }

    @Override
    public void generate() {
        dropWithUglySteelPlating(ModBlocks.autoHammer.asBlock());
        dropWithUglySteelPlating(ModBlocks.autoCompressedHammer.asBlock());
        dropWithUglySteelPlating(ModBlocks.autoSieve.asBlock());
        dropWithUglySteelPlating(ModBlocks.autoHeavySieve.asBlock());

        dropSelf(ModBlocks.autoCompressor.asBlock());
        dropSelf(ModBlocks.rationingAutoCompressor.asBlock());
        for (final var bait : ModBlocks.baits.values()) {
            dropSelf(bait.asBlock());
        }
        for (final var compressedBlock : ModBlocks.compressedBlocks.values()) {
            dropSelf(compressedBlock.asBlock());
        }
        for (final var heavySieve : ModBlocks.heavySieves.values()) {
            dropSelf(heavySieve.asBlock());
        }
        for (final var woodenCrucible : ModBlocks.woodenCrucibles.values()) {
            dropSelf(woodenCrucible.asBlock());
        }
    }

    private void dropWithUglySteelPlating(Block block) {
        add(block, LootTable.lootTable()
                .withPool(applyExplosionCondition(block, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1f))
                        .add(LootItem.lootTableItem(block))))
                .withPool(applyExplosionCondition(block, LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1f))
                        .add(LootItem.lootTableItem(ModItems.uglySteelPlating))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(ModBlockStateProperties.UGLY, "true"))))));
    }
}
