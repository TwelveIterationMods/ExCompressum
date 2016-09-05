package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemUncompressedCoal extends Item {

    public ItemUncompressedCoal() {
        setRegistryName("uncompressed_coal");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

    public static void registerRecipes(Configuration config) {
        if(config.getBoolean("Uncompressed Coal", "items", true, "If set to true, coal can be crafted into eight pieces of Uncompressed Coal which can smelt one item at a time in a furnace.")) {
            GameRegistry.addShapelessRecipe(new ItemStack(ModItems.uncompressedCoal, 8), Items.coal);
            GameRegistry.addShapelessRecipe(new ItemStack(Items.coal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal), new ItemStack(ModItems.uncompressedCoal));
        }
        GameRegistry.registerFuelHandler(new IFuelHandler() {
            @Override
            public int getBurnTime(ItemStack itemStack) {
                if(itemStack.getItem() == ModItems.uncompressedCoal) {
                    return 200;
                }
                return 0;
            }
        });
    }

}
