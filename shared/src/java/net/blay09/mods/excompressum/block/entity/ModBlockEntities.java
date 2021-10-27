package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static DeferredObject<BlockEntityType<AutoHammerBlockEntity>> autoHammer;
    public static DeferredObject<BlockEntityType<AutoCompressedHammerBlockEntity>> autoCompressedHammer;
    public static DeferredObject<BlockEntityType<AutoCompressorBlockEntity>> autoCompressor;
    public static DeferredObject<BlockEntityType<RationingAutoCompressorBlockEntity>> rationingAutoCompressor;
    public static DeferredObject<BlockEntityType<AutoSieveBlockEntity>> autoSieve;
    public static DeferredObject<BlockEntityType<?>> manaSieve;
    public static DeferredObject<BlockEntityType<HeavySieveBlockEntity>> heavySieve;
    public static DeferredObject<BlockEntityType<AutoHeavySieveBlockEntity>> autoHeavySieve;
    public static DeferredObject<BlockEntityType<WoodenCrucibleBlockEntity>> woodenCrucible;
    public static DeferredObject<BlockEntityType<BaitBlockEntity>> bait;
    public static DeferredObject<BlockEntityType<?>> evolvedOrechid;

    public static void initialize(BalmBlockEntities blockEntities) {
        autoHammer = blockEntities.registerBlockEntity(id("auto_hammer"), AutoHammerBlockEntity::new, ModBlocks.autoHammer);
        autoCompressedHammer = blockEntities.registerBlockEntity(id("auto_compressed_hammer"), AutoCompressedHammerBlockEntity::new, ModBlocks.autoCompressedHammer);
        autoCompressor = blockEntities.registerBlockEntity(id("auto_compressor"), AutoCompressorBlockEntity::new, ModBlocks.autoCompressor);
        rationingAutoCompressor = blockEntities.registerBlockEntity(id("rationing_auto_compressor"), RationingAutoCompressorBlockEntity::new, ModBlocks.rationingAutoCompressor);
        autoSieve = blockEntities.registerBlockEntity(id("auto_sieve"), AutoSieveBlockEntity::new, ModBlocks.autoSieve);
        heavySieve = blockEntities.registerBlockEntity(id("heavy_sieve"), HeavySieveBlockEntity::new, ModBlocks.heavySieves);
        autoHeavySieve = blockEntities.registerBlockEntity(id("auto_heavy_sieve"), AutoHeavySieveBlockEntity::new, ModBlocks.autoHeavySieve);
        woodenCrucible = blockEntities.registerBlockEntity(id("wooden_crucible"), WoodenCrucibleBlockEntity::new, ModBlocks.woodenCrucibles);
        bait = blockEntities.registerBlockEntity(id("bait"), BaitBlockEntity::new, ModBlocks.baits);
        // TODO evolvedOrechid = blockEntities.registerBlockEntity(id("evolved_orechid"), BotaniaCompat::createOrechidTileEntity, ModBlocks.evolvedOrechid);
        // TODO manaSieve = blockEntities.registerBlockEntity(id("mana_sieve"), BotaniaCompat::createManaSieveTileEntity, ModBlocks.manaSieve);
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(ExCompressum.MOD_ID, path);
    }

}
