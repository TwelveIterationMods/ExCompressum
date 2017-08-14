package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockWoodenCrucible extends ItemBlock {

    public ItemBlockWoodenCrucible(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return "tile." + ExCompressum.MOD_ID + ":wooden_crucible_" + itemStack.getMetadata();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
