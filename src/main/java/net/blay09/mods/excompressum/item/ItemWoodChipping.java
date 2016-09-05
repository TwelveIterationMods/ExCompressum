package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.item.Item;

public class ItemWoodChipping extends Item {

    public ItemWoodChipping() {
        setRegistryName("wood_chipping");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

}
