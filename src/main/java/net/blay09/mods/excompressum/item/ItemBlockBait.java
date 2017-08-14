package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockBait;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBait extends ItemBlock {

    public ItemBlockBait(Block block) {
        super(block);
        setHasSubtypes(true);
        setMaxStackSize(1);
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
