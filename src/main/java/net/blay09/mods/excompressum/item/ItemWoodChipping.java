package net.blay09.mods.excompressum.item;

import exnihilo.registries.CompostRegistry;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Color;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemWoodChipping extends Item {

    public ItemWoodChipping() {
        setUnlocalizedName(ExCompressum.MOD_ID + ":wood_chippings");
        setTextureName(ExCompressum.MOD_ID + ":wood_chippings");
        setCreativeTab(ExCompressum.creativeTab);
    }

    public static void registerRecipes(Configuration config) {
        if(config.getBoolean("Wood Chippings", "items", true, "If set to true, wood can be smashed into wood chippings, which can be composted into dirt.")) {
            List<ItemStack> woodLogs = OreDictionary.getOres("logWood", false);
            for(ItemStack itemStack : woodLogs) {
                HammerRegistry.register(Block.getBlockFromItem(itemStack.getItem()), itemStack.getItemDamage(), ModItems.woodChipping, 0, 1f, 0f);
                HammerRegistry.register(Block.getBlockFromItem(itemStack.getItem()), itemStack.getItemDamage(), ModItems.woodChipping, 0, 0.5f, 0f);
                HammerRegistry.register(Block.getBlockFromItem(itemStack.getItem()), itemStack.getItemDamage(), ModItems.woodChipping, 0, 0.25f, 0f);
            }
            CompostRegistry.register(Item.getItemFromBlock(Blocks.brown_mushroom), 0, 0.125f, new Color("CFBFB6"));
        }
    }

}
