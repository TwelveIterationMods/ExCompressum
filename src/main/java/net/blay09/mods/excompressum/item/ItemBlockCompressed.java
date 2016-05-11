package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockCompressed extends ItemBlock {
    public ItemBlockCompressed(Block block) {
        super(block);
        setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        switch(itemStack.getItemDamage()) {
            case 0: return "tile." + ExCompressum.MOD_ID + ":compressed_dust";
            case 1: return "tile." + ExCompressum.MOD_ID + ":compressed_cobblestone";
            case 2: return "tile." + ExCompressum.MOD_ID + ":compressed_gravel";
            case 3: return "tile." + ExCompressum.MOD_ID + ":compressed_sand";
            case 4: return "tile." + ExCompressum.MOD_ID + ":compressed_dirt";
            case 5: return "tile." + ExCompressum.MOD_ID + ":compressed_flint";
            case 6: return "tile." + ExCompressum.MOD_ID + ":compressed_stone";
            case 7: return "tile." + ExCompressum.MOD_ID + ":compressed_netherrack";
        }
        return "tile." + ExCompressum.MOD_ID + ":compressed_dust";
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
