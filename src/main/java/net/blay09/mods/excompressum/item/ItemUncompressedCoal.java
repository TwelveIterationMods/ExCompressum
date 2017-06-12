package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;

public class ItemUncompressedCoal extends ItemCompressum {

    public ItemUncompressedCoal() {
        setRegistryName("uncompressed_coal");
        setUnlocalizedName(getRegistryNameString());
        setCreativeTab(ExCompressum.creativeTab);
    }

}
