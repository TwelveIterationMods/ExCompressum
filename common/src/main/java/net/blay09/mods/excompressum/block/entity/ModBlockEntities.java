package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.world.level.block.entity.BalmBlockEntityTypeRegistrar;
import net.blay09.mods.excompressum.block.*;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static Holder<BlockEntityType<AutoHammerBlockEntity>> autoHammer;
    public static Holder<BlockEntityType<AutoCompressedHammerBlockEntity>> autoCompressedHammer;
    public static Holder<BlockEntityType<AutoCompressorBlockEntity>> autoCompressor;
    public static Holder<BlockEntityType<RationingAutoCompressorBlockEntity>> rationingAutoCompressor;
    public static Holder<BlockEntityType<AutoSieveBlockEntity>> autoSieve;
    public static Holder<BlockEntityType<HeavySieveBlockEntity>> heavySieve;
    public static Holder<BlockEntityType<AutoHeavySieveBlockEntity>> autoHeavySieve;
    public static Holder<BlockEntityType<WoodenCrucibleBlockEntity>> woodenCrucible;
    public static Holder<BlockEntityType<BaitBlockEntity>> bait;

    public static void initialize(BalmBlockEntityTypeRegistrar blockEntities) {
        autoHammer = blockEntities.register("auto_hammer", AutoHammerBlockEntity::new, ModBlocks.autoHammer).asHolder();
        autoCompressedHammer = blockEntities.register("auto_compressed_hammer", AutoCompressedHammerBlockEntity::new, ModBlocks.autoCompressedHammer).asHolder();
        autoCompressor = blockEntities.register("auto_compressor", AutoCompressorBlockEntity::new, ModBlocks.autoCompressor).asHolder();
        rationingAutoCompressor = blockEntities.register("rationing_auto_compressor", RationingAutoCompressorBlockEntity::new, ModBlocks.rationingAutoCompressor).asHolder();
        autoSieve = blockEntities.register("auto_sieve", AutoSieveBlockEntity::new, ModBlocks.autoSieve).asHolder();
        heavySieve = blockEntities.register("heavy_sieve", HeavySieveBlockEntity::new, ModBlocks.heavySieves.values()).asHolder();
        autoHeavySieve = blockEntities.register("auto_heavy_sieve", AutoHeavySieveBlockEntity::new, ModBlocks.autoHeavySieve).asHolder();
        woodenCrucible = blockEntities.register("wooden_crucible", WoodenCrucibleBlockEntity::new,  ModBlocks.woodenCrucibles.values()).asHolder();
        bait = blockEntities.register("bait", BaitBlockEntity::new, ModBlocks.baits.values()).asHolder();
    }

}
