package net.blay09.mods.excompressum.block;

import net.blay09.mods.balm.world.level.block.BalmBlockRegistrar;
import net.blay09.mods.balm.world.level.block.DeferredBlock;
import net.blay09.mods.balm.world.level.block.DiscriminatedBlocks;

public class ModBlocks {

    public static DiscriminatedBlocks<CompressedBlockType> compressedBlocks;
    public static DiscriminatedBlocks<HeavySieveType> heavySieves;
    public static DiscriminatedBlocks<WoodenCrucibleType> woodenCrucibles;
    public static DiscriminatedBlocks<BaitType> baits;
    public static DeferredBlock autoHammer;
    public static DeferredBlock autoCompressedHammer;
    public static DeferredBlock autoHeavySieve;
    public static DeferredBlock autoSieve;
    public static DeferredBlock autoCompressor;
    public static DeferredBlock rationingAutoCompressor;

    public static void initialize(BalmBlockRegistrar blocks) {
        autoHammer = blocks.register("auto_hammer", AutoHammerBlock::new, it -> it)
                .withDefaultItem().asDeferredBlock();
        autoSieve = blocks.register("auto_sieve", AutoSieveBlock::new, it -> it)
                .withDefaultItem().asDeferredBlock();
        autoCompressedHammer = blocks.register("auto_compressed_hammer", AutoCompressedHammerBlock::new, it -> it)
                .withDefaultItem().asDeferredBlock();
        autoHeavySieve = blocks.register("auto_heavy_sieve", AutoHeavySieveBlock::new, it -> it)
                .withDefaultItem().asDeferredBlock();
        autoCompressor = blocks.register("auto_compressor", AutoCompressorBlock::new, it -> it)
                .withDefaultItem().asDeferredBlock();
        rationingAutoCompressor = blocks.register("rationing_auto_compressor", RationingAutoCompressorBlock::new, it -> it)
                .withDefaultItem().asDeferredBlock();

        compressedBlocks = blocks.registerDiscriminated(
                CompressedBlockType.values(),
                it -> DiscriminatedBlocks.suffix("compressed_", it),
                CompressedBlock::new, it -> it).asDiscriminatedBlocks();
        heavySieves = blocks.registerDiscriminated(
                HeavySieveType.values(),
                it -> DiscriminatedBlocks.prefix(it, "heavy_sieve"),
                HeavySieveBlock::new, it -> it).asDiscriminatedBlocks();
        woodenCrucibles = blocks.registerDiscriminated(
                WoodenCrucibleType.values(),
                it -> DiscriminatedBlocks.prefix(it, "crucible"),
                WoodenCrucibleBlock::new, it -> it).asDiscriminatedBlocks();
        baits = blocks.registerDiscriminated(
                BaitType.values(),
                it -> DiscriminatedBlocks.prefix(it, "bait"),
                BaitBlock::new, it -> it).asDiscriminatedBlocks();
    }

}
