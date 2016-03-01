package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemHeavySilkMesh extends Item {

    public ItemHeavySilkMesh() {
        setCreativeTab(ExCompressum.creativeTab);
        setUnlocalizedName(ExCompressum.MOD_ID + ":heavy_silk_mesh");
        setTextureName(ExCompressum.MOD_ID + ":heavy_silk_mesh");
    }

}
