package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.RationingAutoCompressorTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;

public class RationingAutoCompressorBlock extends AutoCompressorBlock {

    public static final String name = "auto_compressor_rationing";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new RationingAutoCompressorTileEntity();
    }

}
