package net.blay09.mods.excompressum;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.block.BlockBait;
import net.blay09.mods.excompressum.block.BlockCompressed;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.blay09.mods.excompressum.block.BlockWoodenCrucible;
import net.blay09.mods.excompressum.item.ItemBlockBait;
import net.blay09.mods.excompressum.item.ItemBlockCompressed;
import net.blay09.mods.excompressum.item.ItemBlockHeavySieve;
import net.blay09.mods.excompressum.item.ItemBlockWoodenCrucible;
import net.blay09.mods.excompressum.tile.TileEntityBait;
import net.blay09.mods.excompressum.tile.TileEntityHeavySieve;
import net.blay09.mods.excompressum.tile.TileEntityWoodenCrucible;

public class ModBlocks {
    public static BlockCompressed compressedBlock;
    public static BlockHeavySieve heavySieve;
    public static BlockWoodenCrucible woodenCrucible;
    public static BlockBait bait;

    public static void init() {
        compressedBlock = new BlockCompressed();
        GameRegistry.registerBlock(compressedBlock, ItemBlockCompressed.class, "compressed_dust"); // god damn it Blay. can't rename because already released
        heavySieve = new BlockHeavySieve();
        GameRegistry.registerBlock(heavySieve, ItemBlockHeavySieve.class, "heavySieve");
        woodenCrucible = new BlockWoodenCrucible();
        GameRegistry.registerBlock(woodenCrucible, ItemBlockWoodenCrucible.class, "woodenCrucible");
        bait = new BlockBait();
        GameRegistry.registerBlock(bait, ItemBlockBait.class, "bait");

        GameRegistry.registerTileEntity(TileEntityWoodenCrucible.class, "woodenCrucible");
        GameRegistry.registerTileEntity(TileEntityHeavySieve.class, ExCompressum.MOD_ID + ":heavy_sieve");
        GameRegistry.registerTileEntity(TileEntityBait.class, "bait");
    }
}
