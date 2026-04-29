package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.blay09.mods.excompressum.block.CompressedBlockType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        final var mineablePickaxe = builder(BlockTags.MINEABLE_WITH_PICKAXE);
        mineablePickaxe.add(ModBlocks.autoHammer.asResourceKey(),
                ModBlocks.autoCompressedHammer.asResourceKey(),
                ModBlocks.autoSieve.asResourceKey(),
                ModBlocks.autoHeavySieve.asResourceKey(),
                ModBlocks.autoCompressor.asResourceKey(),
                ModBlocks.rationingAutoCompressor.asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.ANDESITE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.COBBLESTONE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.DIORITE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.END_STONE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.GRANITE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.FLINT).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK).asResourceKey());

        final var mineableShovel = builder(BlockTags.MINEABLE_WITH_SHOVEL);
        mineableShovel.add(ModBlocks.compressedBlocks.get(CompressedBlockType.DIRT).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.GRAVEL).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.SAND).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_ANDESITE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_DIORITE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_END_STONE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_GRANITE).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_NETHERRACK).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.DUST).asResourceKey(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.SOUL_SAND).asResourceKey());

        final var mineableAxe = builder(BlockTags.MINEABLE_WITH_AXE);
        ModBlocks.heavySieves.sortedValues().map(DeferredBlock::asResourceKey).forEach(mineableAxe::add);
        ModBlocks.woodenCrucibles.sortedValues().map(DeferredBlock::asResourceKey).forEach(mineableAxe::add);

        builder(ModBlockTags.MINEABLE_WITH_CROOK).addOptionalTag(BlockTags.LEAVES);
        builder(ModBlockTags.MINEABLE_WITH_HAMMER)
                .addOptionalTag(BlockTags.LOGS)
                .addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("exdeorum", "mineable/hammer")))
                .add(BlockItemIds.ANDESITE,
                        BlockItemIds.COBBLESTONE,
                        BlockItemIds.DIORITE,
                        BlockItemIds.END_STONE,
                        BlockItemIds.GRANITE,
                        BlockItemIds.GRAVEL,
                        BlockItemIds.NETHERRACK,
                        BlockItemIds.SAND)
                .add(ModBlocks.compressedBlocks.get(CompressedBlockType.ANDESITE).asResourceKey(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.COBBLESTONE).asResourceKey(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.DIORITE).asResourceKey(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.END_STONE).asResourceKey(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.GRANITE).asResourceKey(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.GRAVEL).asResourceKey(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK).asResourceKey(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.SAND).asResourceKey());

        builder(ModBlockTags.MINEABLE_WITH_CHICKEN_STICK).addTag(ModBlockTags.MINEABLE_WITH_HAMMER);

        builder(ModBlockTags.INCORRECT_FOR_CHICKEN_STICK);
    }

}
