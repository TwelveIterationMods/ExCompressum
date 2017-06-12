package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockHeavySieve extends ItemBlock {
    public ItemBlockHeavySieve(BlockHeavySieve block) {
        super(block);
        setRegistryName(block.getRegistryNameString());
        setHasSubtypes(true);
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return "tile." + ExCompressum.MOD_ID + ":heavy_sieve_" + itemStack.getItemDamage();
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }
}
