package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

public class ItemDoubleCompressedDiamondHammer extends Item {

    public ItemDoubleCompressedDiamondHammer() {
        setRegistryName("double_compressed_diamond_hammer");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List<String> list, boolean flag) {
        list.add(TextFormatting.DARK_AQUA + I18n.format("tooltip.excompressum:double_compressed_diamond_hammer"));
        list.add(TextFormatting.DARK_AQUA + I18n.format("tooltip.excompressum:best_with_blasting"));
    }

}
