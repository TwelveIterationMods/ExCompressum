package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.Item;

public class ItemUncompressedCoal extends Item {

    public ItemUncompressedCoal() {
        setRegistryName("uncompressed_coal");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

}
