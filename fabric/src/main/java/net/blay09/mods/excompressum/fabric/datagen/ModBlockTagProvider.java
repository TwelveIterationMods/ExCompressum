package net.blay09.mods.excompressum.fabric.datagen;

import net.blay09.mods.balm.world.level.block.BlockLike;
import net.blay09.mods.excompressum.block.CompressedBlockType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.tag.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public ModBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        final var mineablePickaxe = valueLookupBuilder(BlockTags.MINEABLE_WITH_PICKAXE);
        mineablePickaxe.add(ModBlocks.autoHammer.asBlock(),
                ModBlocks.autoCompressedHammer.asBlock(),
                ModBlocks.autoSieve.asBlock(),
                ModBlocks.autoHeavySieve.asBlock(),
                ModBlocks.autoCompressor.asBlock(),
                ModBlocks.rationingAutoCompressor.asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.ANDESITE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.COBBLESTONE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.DIORITE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.END_STONE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.GRANITE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.FLINT).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK).asBlock());

        final var mineableShovel = valueLookupBuilder(BlockTags.MINEABLE_WITH_SHOVEL);
        mineableShovel.add(ModBlocks.compressedBlocks.get(CompressedBlockType.DIRT).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.GRAVEL).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.SAND).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_ANDESITE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_DIORITE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_END_STONE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_GRANITE).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.CRUSHED_NETHERRACK).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.DUST).asBlock(),
                ModBlocks.compressedBlocks.get(CompressedBlockType.SOUL_SAND).asBlock());

        final var mineableAxe = valueLookupBuilder(BlockTags.MINEABLE_WITH_AXE);
        ModBlocks.heavySieves.sortedValues().map(BlockLike::asBlock).forEach(mineableAxe::add);
        ModBlocks.woodenCrucibles.sortedValues().map(BlockLike::asBlock).forEach(mineableAxe::add);

        valueLookupBuilder(ModBlockTags.MINEABLE_WITH_CROOK).addOptionalTag(BlockTags.LEAVES);
        valueLookupBuilder(ModBlockTags.MINEABLE_WITH_HAMMER)
                .addOptionalTag(BlockTags.LOGS)
                .addOptionalTag(TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath("exdeorum", "mineable/hammer")))
                .add(Blocks.ANDESITE,
                        Blocks.COBBLESTONE,
                        Blocks.DIORITE,
                        Blocks.END_STONE,
                        Blocks.GRANITE,
                        Blocks.GRAVEL,
                        Blocks.NETHERRACK,
                        Blocks.SAND,
                        ModBlocks.compressedBlocks.get(CompressedBlockType.ANDESITE).asBlock(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.COBBLESTONE).asBlock(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.DIORITE).asBlock(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.END_STONE).asBlock(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.GRANITE).asBlock(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.GRAVEL).asBlock(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.NETHERRACK).asBlock(),
                        ModBlocks.compressedBlocks.get(CompressedBlockType.SAND).asBlock());

        valueLookupBuilder(ModBlockTags.MINEABLE_WITH_CHICKEN_STICK).addTag(ModBlockTags.MINEABLE_WITH_HAMMER);

        valueLookupBuilder(ModBlockTags.INCORRECT_FOR_CHICKEN_STICK);
    }

}
