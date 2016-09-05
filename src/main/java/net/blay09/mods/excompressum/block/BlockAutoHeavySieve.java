package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.tile.TileAutoHeavySieve;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAutoHeavySieve extends BlockAutoSieve {

    public BlockAutoHeavySieve() {
        setRegistryName("auto_heavy_sieve");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAutoHeavySieve();
    }

}
