package net.blay09.mods.excompressum.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class BlockCompressedDust extends Block {

    public BlockCompressedDust() {
        super(Material.sand);
        setHardness(4f);
        setResistance(6f);
        setStepSound(soundTypeSnow);
        setCreativeTab(CreativeTabs.tabBlock);
        setBlockName(ExCompressum.MOD_ID + ":compressed_dust");
        setBlockTextureName(ExCompressum.MOD_ID + ":compressed_dust");
    }

    public static void registerRecipes(Configuration config) {
        if (config.getBoolean("Compressed Dust", "blocks", true, "Set this to false to disable the recipe for the compressed dust.")) {
            GameRegistry.addRecipe(new ItemStack(ExCompressum.compressedDust), "###", "###", "###", '#', GameRegistry.findBlock("exnihilo", "dust"));
            GameRegistry.addShapelessRecipe(new ItemStack(GameRegistry.findBlock("exnihilo", "dust"), 9), ExCompressum.compressedDust);
        }
    }
}
