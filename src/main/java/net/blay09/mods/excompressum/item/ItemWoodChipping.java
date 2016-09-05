package net.blay09.mods.excompressum.item;

import exnihilo.registries.CompostRegistry;
import exnihilo.registries.HammerRegistry;
import exnihilo.registries.helpers.Color;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class ItemWoodChipping extends Item {

    public ItemWoodChipping() {
        setRegistryName("wood_chipping");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

    public static void registerRecipes(Configuration config) {
        if(config.getBoolean("Wood Chippings", "items", true, "If set to true, wood can be smashed into wood chippings, which can be composted into dirt.")) {
            for (int i = 0; i < 4; i++) {
                for(int j = 0; j <= 8; j += 4) {
                    HammerRegistry.register(Blocks.log, i | j, ModItems.woodChipping, 0, 1f, 0f);
                    HammerRegistry.register(Blocks.log, i | j, ModItems.woodChipping, 0, 0.75f, 0f);
                    HammerRegistry.register(Blocks.log, i | j, ModItems.woodChipping, 0, 0.5f, 0f);
                    HammerRegistry.register(Blocks.log, i | j, ModItems.woodChipping, 0, 0.25f, 0f);
                }
            }
            for (int i = 0; i < 2; i++) {
                for(int j = 0; j <= 8; j += 4) {
                    HammerRegistry.register(Blocks.log2, i | j, ModItems.woodChipping, 0, 1f, 0f);
                    HammerRegistry.register(Blocks.log2, i | j, ModItems.woodChipping, 0, 0.75f, 0f);
                    HammerRegistry.register(Blocks.log2, i | j, ModItems.woodChipping, 0, 0.5f, 0f);
                    HammerRegistry.register(Blocks.log2, i | j, ModItems.woodChipping, 0, 0.25f, 0f);
                }
            }
            List<ItemStack> oreDictStacks = OreDictionary.getOres("dustWood", false);
            for(ItemStack itemStack : oreDictStacks) {
                CompostRegistry.register(itemStack.getItem(), itemStack.getItemDamage(), 0.125f, new Color("C77826"));
            }
        }
    }

}
