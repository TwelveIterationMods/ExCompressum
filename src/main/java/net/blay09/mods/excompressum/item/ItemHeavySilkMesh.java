package net.blay09.mods.excompressum.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class ItemHeavySilkMesh extends Item {

    public ItemHeavySilkMesh() {
        setCreativeTab(ExCompressum.creativeTab);
        setUnlocalizedName(ExCompressum.MOD_ID + ":heavy_silk_mesh");
        setTextureName(ExCompressum.MOD_ID + ":heavy_silk_mesh");
    }

    public static void registerRecipes(Configuration config) {
        Item itemSilkMesh = GameRegistry.findItem("exnihilo", "mesh");
        if (itemSilkMesh != null) {
            GameRegistry.addRecipe(new ItemStack(ModItems.heavySilkMesh), "##", "##", '#', itemSilkMesh);
        }
    }
}
