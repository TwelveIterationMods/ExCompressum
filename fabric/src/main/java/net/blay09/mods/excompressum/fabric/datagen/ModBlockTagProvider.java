package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.excompressum.block.CompressedBlockType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagProvider<Block> {
    public ModBlockTagProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, Registries.BLOCK, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        final var mineablePickaxe = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE);
        mineablePickaxe.add(ModBlocks.autoHammer,
                ModBlocks.autoCompressedHammer,
                ModBlocks.autoSieve,
                ModBlocks.autoHeavySieve,
                ModBlocks.autoCompressor,
                ModBlocks.rationingAutoCompressor,
                ModBlocks.compressedBlocks[CompressedBlockType.ANDESITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.COBBLESTONE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.DIORITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.END_STONE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.GRANITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.FLINT.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.NETHERRACK.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.NETHERRACK.ordinal()]);

        final var mineableShovel = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_SHOVEL);
        mineableShovel.add(ModBlocks.compressedBlocks[CompressedBlockType.DIRT.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.GRAVEL.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.SAND.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_ANDESITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_DIORITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_END_STONE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_GRANITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.CRUSHED_NETHERRACK.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.DUST.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.SOUL_SAND.ordinal()]);

        final var mineableAxe = getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE);
        for (final var heavySieve : ModBlocks.heavySieves) {
            mineableAxe.add(heavySieve);
        }
        for (final var woodenCrucible : ModBlocks.woodenCrucibles) {
            mineableAxe.add(woodenCrucible);
        }

        getOrCreateTagBuilder(ModBlockTags.MINEABLE_WITH_CROOK).addOptionalTag(BlockTags.LEAVES);
        getOrCreateTagBuilder(ModBlockTags.MINEABLE_WITH_HAMMER).addOptionalTag(BlockTags.LOGS).add(
                Blocks.ANDESITE,
                Blocks.COBBLESTONE,
                Blocks.DIORITE,
                Blocks.END_STONE,
                Blocks.GRANITE,
                Blocks.GRAVEL,
                Blocks.NETHERRACK,
                Blocks.SAND,
                ModBlocks.compressedBlocks[CompressedBlockType.ANDESITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.COBBLESTONE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.DIORITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.END_STONE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.GRANITE.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.GRAVEL.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.NETHERRACK.ordinal()],
                ModBlocks.compressedBlocks[CompressedBlockType.SAND.ordinal()]);
    }

}
