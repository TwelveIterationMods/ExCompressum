package net.blay09.mods.excompressum;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockCompressedDust extends Block {

    public BlockCompressedDust() {
        super(Material.sand);
        setHardness(4f);
        setResistance(6f);
        setStepSound(soundTypeSnow);
        setCreativeTab(CreativeTabs.tabBlock);
        setBlockName(ExCompressum.MOD_ID + ":compressed_dust");
        setBlockTextureName(ExCompressum.MOD_ID + ":compressed_dust");
    }

}
