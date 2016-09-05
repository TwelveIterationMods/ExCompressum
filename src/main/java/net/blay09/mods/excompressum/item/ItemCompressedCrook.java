package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ExCompressumConfig;
import net.blay09.mods.excompressum.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.config.Configuration;

public class ItemCompressedCrook extends Item {

    public ItemCompressedCrook() {
        setRegistryName("compressed_crook");
        setUnlocalizedName(getRegistryName().toString());
        setMaxDamage((int) (ToolMaterial.WOOD.getMaxUses() * 2 * ExCompressumConfig.compressedCrookDurabilityMultiplier));
        setCreativeTab(ExCompressum.creativeTab);
    }

    // TODO implement me senpai

}
