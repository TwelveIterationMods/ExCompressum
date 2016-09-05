package net.blay09.mods.excompressum;

import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.item.*;
import net.blay09.mods.excompressum.tile.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModAPIManager;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static BlockCompressed compressedBlock;
    public static BlockHeavySieve heavySieve;
    public static BlockWoodenCrucible woodenCrucible;
    public static BlockBait bait;
    public static BlockAutoHammer autoHammer;
    public static BlockAutoCompressedHammer autoCompressedHammer;
    public static BlockAutoHeavySieve autoHeavySieve;
    public static BlockAutoSieve autoSieve;
    public static BlockManaSieve manaSieve;
    public static BlockAutoCompressor autoCompressor;

    public static void init() {
        compressedBlock = new BlockCompressed(); // TODO omniafy
        GameRegistry.register(compressedBlock);
        GameRegistry.register(new ItemBlockCompressed(compressedBlock).setRegistryName(compressedBlock.getRegistryName()));

        heavySieve = new BlockHeavySieve(); // TODO omniafy
        GameRegistry.register(heavySieve);
        GameRegistry.register(new ItemBlockHeavySieve(heavySieve).setRegistryName(heavySieve.getRegistryName()));

        woodenCrucible = new BlockWoodenCrucible();
        GameRegistry.register(woodenCrucible);
        GameRegistry.register(new ItemBlockWoodenCrucible(woodenCrucible).setRegistryName(woodenCrucible.getRegistryName()));

        bait = new BlockBait();
        GameRegistry.register(bait);
        GameRegistry.register(new ItemBlockBait(bait).setRegistryName(bait.getRegistryName()));

        autoHammer = new BlockAutoHammer();
        autoCompressedHammer = new BlockAutoCompressedHammer();
        autoSieve = new BlockAutoSieve(); // TODO omniafy
        autoHeavySieve = new BlockAutoHeavySieve(); // TODO omniafy
        autoCompressor = new BlockAutoCompressor();
        if(ModAPIManager.INSTANCE.hasAPI("CoFHAPI")) { // TODO Tesla? Or what's the cool new thing for power now?
            registerDefaultBlock(autoHammer);
            registerDefaultBlock(autoCompressedHammer);
            GameRegistry.register(autoSieve);
            GameRegistry.register(new ItemBlockAutoSieve(autoSieve).setRegistryName(autoSieve.getRegistryName()));
            GameRegistry.register(autoHeavySieve);
            GameRegistry.register(new ItemBlockAutoHeavySieve(autoHeavySieve).setRegistryName(autoHeavySieve.getRegistryName()));
            registerDefaultBlock(autoCompressor);
        }

        manaSieve = new BlockManaSieve();
        if(Loader.isModLoaded(Compat.BOTANIA)) {
            GameRegistry.register(manaSieve);
            GameRegistry.register(new ItemBlockManaSieve(manaSieve));
        }

        GameRegistry.registerTileEntity(TileWoodenCrucible.class, ExCompressum.MOD_ID + ":wooden_crucible");
        GameRegistry.registerTileEntity(TileHeavySieve.class, ExCompressum.MOD_ID + ":heavy_sieve");
        GameRegistry.registerTileEntity(TileBait.class, ExCompressum.MOD_ID + ":bait");

        if(ModAPIManager.INSTANCE.hasAPI("CoFHAPI")) { // TODO Tesla? Or what's the cool new thing for power now?
            GameRegistry.registerTileEntity(TileAutoHammer.class, ExCompressum.MOD_ID + "auto_hammer");
            GameRegistry.registerTileEntity(TileAutoCompressedHammer.class, ExCompressum.MOD_ID + "auto_compressed_hammer");
            GameRegistry.registerTileEntity(TileAutoSieve.class, ExCompressum.MOD_ID + "auto_sieve");
            GameRegistry.registerTileEntity(TileAutoHeavySieve.class, ExCompressum.MOD_ID + "auto_heavy_sieve");
            GameRegistry.registerTileEntity(TileEntityAutoCompressor.class, ExCompressum.MOD_ID + "auto_compressor");
        }

        if(Loader.isModLoaded(Compat.BOTANIA)) {
            GameRegistry.registerTileEntity(TileAutoSieveMana.class, ExCompressum.MOD_ID + "mana_sieve");
        }
    }

    private static void registerDefaultBlock(Block block) {
        GameRegistry.register(block);
        GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

}
