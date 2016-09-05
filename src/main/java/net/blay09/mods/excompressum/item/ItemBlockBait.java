package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ItemBlockBait extends ItemBlock {

    public ItemBlockBait(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxStackSize(1);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean debug) {
        if(itemStack.getItemDamage() == 6) {
            list.add(I18n.format("info.excompressum:baitPlaceInWater"));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        return "item." + ExCompressum.MOD_ID + ":bait_" + itemStack.getItemDamage();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
