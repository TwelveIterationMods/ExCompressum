package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockBait;
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
        BlockBait.Type type = BlockBait.Type.fromId(itemStack.getItemDamage());
        if(type == BlockBait.Type.SQUID) {
            list.add(I18n.format("info.excompressum:baitPlaceInWater"));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        BlockBait.Type type = BlockBait.Type.fromId(itemStack.getItemDamage());
        return "item." + ExCompressum.MOD_ID + ":bait_" + (type != null ? type.getName() : "unknown");
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
