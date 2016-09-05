package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.tile.TileEntityAutoSieveMana;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockManaSieve extends BlockAutoSieveBase {

    public BlockManaSieve() {
        super(Material.IRON);
        setRegistryName("mana_sieve");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoSieveMana();
    }

}
