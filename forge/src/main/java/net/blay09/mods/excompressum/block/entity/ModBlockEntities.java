package net.blay09.mods.excompressum.block.entity;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.block.BalmBlockEntities;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.compat.botania.BotaniaCompat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ModBlockEntities {
    public static DeferredObject<BlockEntityType<AutoHammerBlockEntity>> autoHammer;
    public static DeferredObject<BlockEntityType<AutoCompressedHammerBlockEntity>> autoCompressedHammer;
    public static DeferredObject<BlockEntityType<AutoCompressorBlockEntity>> autoCompressor;
    public static DeferredObject<BlockEntityType<RationingAutoCompressorBlockEntity>> rationingAutoCompressor;
    public static DeferredObject<BlockEntityType<AutoSieveBlockEntity>> autoSieve;
    public static DeferredObject<BlockEntityType<BlockEntity>> manaSieve;
    public static DeferredObject<BlockEntityType<HeavySieveBlockEntity>> heavySieve;
    public static DeferredObject<BlockEntityType<AutoHeavySieveBlockEntity>> autoHeavySieve;
    public static DeferredObject<BlockEntityType<WoodenCrucibleBlockEntity>> woodenCrucible;
    public static DeferredObject<BlockEntityType<BaitBlockEntity>> bait;
    public static DeferredObject<BlockEntityType<BlockEntity>> evolvedOrechid;

    public static void initialize(BalmBlockEntities blockEntities) {
        autoHammer = blockEntities.registerBlockEntity(id("auto_hammer"), AutoHammerBlockEntity::new, () -> new Block[]{ModBlocks.autoHammer});
        autoCompressedHammer = blockEntities.registerBlockEntity(id("auto_compressed_hammer"), AutoCompressedHammerBlockEntity::new, () -> new Block[]{ModBlocks.autoCompressedHammer});
        autoCompressor = blockEntities.registerBlockEntity(id("auto_compressor"), AutoCompressorBlockEntity::new, () -> new Block[]{ModBlocks.autoCompressor});
        rationingAutoCompressor = blockEntities.registerBlockEntity(id("rationing_auto_compressor"), RationingAutoCompressorBlockEntity::new, () -> new Block[]{ModBlocks.rationingAutoCompressor});
        autoSieve = blockEntities.registerBlockEntity(id("auto_sieve"), AutoSieveBlockEntity::new, () -> new Block[]{ModBlocks.autoSieve});
        heavySieve = blockEntities.registerBlockEntity(id("heavy_sieve"), HeavySieveBlockEntity::new, () -> ModBlocks.heavySieves);
        autoHeavySieve = blockEntities.registerBlockEntity(id("auto_heavy_sieve"), AutoHeavySieveBlockEntity::new, () -> new Block[]{ModBlocks.autoHeavySieve});
        woodenCrucible = blockEntities.registerBlockEntity(id("wooden_crucible"), WoodenCrucibleBlockEntity::new, () -> ModBlocks.woodenCrucibles);
        bait = blockEntities.registerBlockEntity(id("bait"), BaitBlockEntity::new, () -> ModBlocks.baits);
        evolvedOrechid = blockEntities.registerBlockEntity(id("evolved_orechid"), BotaniaCompat::createOrechidTileEntity, () -> new Block[]{ModBlocks.evolvedOrechid});
        manaSieve = blockEntities.registerBlockEntity(id("mana_sieve"), BotaniaCompat::createManaSieveTileEntity, () -> new Block[]{ModBlocks.manaSieve});
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(ExCompressum.MOD_ID, path);
    }

}
