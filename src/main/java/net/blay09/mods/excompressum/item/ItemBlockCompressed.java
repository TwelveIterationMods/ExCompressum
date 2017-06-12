package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockCompressed;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCompressed extends ItemBlock {
    public ItemBlockCompressed(BlockCompressed block) {
        super(block);
        setRegistryName(block.getRegistryNameString());
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        BlockCompressed.Type type = BlockCompressed.Type.fromId(itemStack.getItemDamage());
        return "tile." + ExCompressum.MOD_ID + ":compressed_" + (type != null ? type.getName() : "dust");
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
