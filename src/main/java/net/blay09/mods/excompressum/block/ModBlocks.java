package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.IRegisterModel;
import net.blay09.mods.excompressum.block.*;
import net.blay09.mods.excompressum.compat.Compat;
import net.blay09.mods.excompressum.item.*;
import net.blay09.mods.excompressum.tile.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
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
        compressedBlock = new BlockCompressed();
        registerBlock(compressedBlock, new ItemBlockCompressed(compressedBlock).setRegistryName(compressedBlock.getRegistryName()));

        heavySieve = new BlockHeavySieve(); // TODO omniafy
        registerBlock(heavySieve, new ItemBlockHeavySieve(heavySieve).setRegistryName(heavySieve.getRegistryName()));

        woodenCrucible = new BlockWoodenCrucible();
        registerBlock(woodenCrucible, new ItemBlockWoodenCrucible(woodenCrucible).setRegistryName(woodenCrucible.getRegistryName()));

        bait = new BlockBait();
        registerBlock(bait, new ItemBlockBait(bait).setRegistryName(bait.getRegistryName()));

        // wow seriously fuck this
        autoHammer = new BlockAutoHammer("auto_hammer");
        autoCompressedHammer = new BlockAutoCompressedHammer();
        autoSieve = new BlockAutoSieve("auto_sieve"); // TODO omniafy
        autoHeavySieve = new BlockAutoHeavySieve(); // TODO omniafy
        autoCompressor = new BlockAutoCompressor();
        if(ModAPIManager.INSTANCE.hasAPI("CoFHAPI")) { // TODO Tesla? Or what's the cool new thing for power now?
            registerDefaultBlock(autoHammer);
            registerDefaultBlock(autoCompressedHammer);
            registerBlock(autoSieve, new ItemBlockAutoSieve(autoSieve).setRegistryName(autoSieve.getRegistryName()));
            registerBlock(autoHeavySieve, new ItemBlockAutoHeavySieve(autoHeavySieve).setRegistryName(autoHeavySieve.getRegistryName()));
            registerDefaultBlock(autoCompressor);
        }

        manaSieve = new BlockManaSieve();
        if(Loader.isModLoaded(Compat.BOTANIA)) {
            registerBlock(manaSieve, new ItemBlockManaSieve(manaSieve).setRegistryName(manaSieve.getRegistryName()));
        }

        GameRegistry.registerTileEntity(TileWoodenCrucible.class, ExCompressum.MOD_ID + ":wooden_crucible");
        GameRegistry.registerTileEntity(TileHeavySieve.class, ExCompressum.MOD_ID + ":heavy_sieve");
        GameRegistry.registerTileEntity(TileBait.class, ExCompressum.MOD_ID + ":bait");

        if(ModAPIManager.INSTANCE.hasAPI("CoFHAPI")) { // TODO Tesla? Or what's the cool new thing for power now?
            GameRegistry.registerTileEntity(TileAutoHammer.class, ExCompressum.MOD_ID + "auto_hammer");
            GameRegistry.registerTileEntity(TileAutoCompressedHammer.class, ExCompressum.MOD_ID + "auto_compressed_hammer.json");
            GameRegistry.registerTileEntity(TileAutoSieve.class, ExCompressum.MOD_ID + "auto_sieve");
            GameRegistry.registerTileEntity(TileAutoHeavySieve.class, ExCompressum.MOD_ID + "auto_heavy_sieve");
            GameRegistry.registerTileEntity(TileEntityAutoCompressor.class, ExCompressum.MOD_ID + "auto_compressor");
        }

        if(Loader.isModLoaded(Compat.BOTANIA)) {
            GameRegistry.registerTileEntity(TileAutoSieveMana.class, ExCompressum.MOD_ID + "mana_sieve");
        }
    }

    private static void registerDefaultBlock(Block block) {
        registerBlock(block, new ItemBlock(block).setRegistryName(block.getRegistryName()));
    }

    private static void registerBlock(Block block, Item itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);
        if(block instanceof IRegisterModel) {
            ((IRegisterModel) block).registerModel(itemBlock);
        } else {
            ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(itemBlock.getRegistryName(), "inventory"));
        }
    }

}
