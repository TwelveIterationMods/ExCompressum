package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.ManaSieveTileEntity;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockReader;

public class ManaSieveBlock extends AutoSieveBaseBlock {

    public static final String name = "mana_sieve";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public ManaSieveBlock() {
        super(Properties.create(Material.IRON)
                .notSolid()
                .setAllowsSpawn((a, b, c, d) -> false)
                .setOpaque((a, b, c) -> false)
                .setSuffocates((a, b, c) -> false)
                .setBlocksVision((a, b, c) -> false));
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new ManaSieveTileEntity();
    }

}
