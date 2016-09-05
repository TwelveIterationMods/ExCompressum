package net.blay09.mods.excompressum.item;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemHeavySilkMesh extends Item {

    // TODO probably disable heavy silk mesh? ... at least until Adscensio

    public ItemHeavySilkMesh() {
        setRegistryName("heavy_silk_mesh");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(ExCompressum.creativeTab);
    }

    public static void registerRecipes(Configuration config) {
        Item itemSilkMesh = GameRegistry.findItem("exnihilo", "mesh");
        if (itemSilkMesh != null) {
            GameRegistry.addRecipe(new ItemStack(ModItems.heavySilkMesh), "##", "##", '#', itemSilkMesh);
        }
    }
}
