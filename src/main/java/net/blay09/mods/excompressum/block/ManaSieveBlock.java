package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.ManaSieveTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class ManaSieveBlock extends BlockAutoSieveBase {

    public static final String name = "mana_sieve";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ManaSieveBlock() {
        super(Material.IRON);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new ManaSieveTileEntity();
    }

}
