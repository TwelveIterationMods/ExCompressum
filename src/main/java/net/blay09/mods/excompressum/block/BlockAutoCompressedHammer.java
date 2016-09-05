package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAutoCompressedHammer extends BlockAutoHammer {

    public BlockAutoCompressedHammer() {
        setRegistryName("auto_compressed_hammer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoCompressedHammer();
    }

}
