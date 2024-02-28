package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.block.AutoSieveBlock;
import net.blay09.mods.excompressum.block.ModBlockStateProperties;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModBlockLootTableProvider extends FabricBlockLootTableProvider {
    protected ModBlockLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        dropWithUglySteelPlating(ModBlocks.autoHammer);
        dropWithUglySteelPlating(ModBlocks.autoCompressedHammer);
        dropWithUglySteelPlating(ModBlocks.autoSieve);
        dropWithUglySteelPlating(ModBlocks.autoHeavySieve);

        dropSelf(ModBlocks.autoCompressor);
        dropSelf(ModBlocks.rationingAutoCompressor);
        for (Block bait : ModBlocks.baits) {
            dropSelf(bait);
        }
        for (Block compressedBlock : ModBlocks.compressedBlocks) {
            dropSelf(compressedBlock);
        }
        for (Block heavySieve : ModBlocks.heavySieves) {
            dropSelf(heavySieve);
        }
        for (Block woodenCrucible : ModBlocks.woodenCrucibles) {
            dropSelf(woodenCrucible);
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
