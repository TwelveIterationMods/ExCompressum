package net.blay09.mods.excompressum.block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class BlockCompressed extends Block {

    private final IIcon[] icons = new IIcon[6];

    public BlockCompressed() {
        super(Material.sand);
        setHardness(4f);
        setResistance(6f);
        setStepSound(soundTypeSnow);
        setCreativeTab(ExCompressum.creativeTab);
        setBlockName(ExCompressum.MOD_ID + ":compressed_dust");
    }

    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getSubBlocks(Item item, CreativeTabs creativeTab, List list) {
        for(int i = 0; i < icons.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        icons[0] = iconRegister.registerIcon(ExCompressum.MOD_ID + ":compressed_dust");
        icons[1] = iconRegister.registerIcon(ExCompressum.MOD_ID + ":compressed_cobblestone");
        icons[2] = iconRegister.registerIcon(ExCompressum.MOD_ID + ":compressed_gravel");
        icons[3] = iconRegister.registerIcon(ExCompressum.MOD_ID + ":compressed_sand");
        icons[4] = iconRegister.registerIcon(ExCompressum.MOD_ID + ":compressed_dirt");
        icons[5] = iconRegister.registerIcon(ExCompressum.MOD_ID + ":compressed_flint");
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return icons[metadata >= 0 && metadata < icons.length ? metadata : 0];
    }

    public static void registerRecipes(Configuration config) {
        boolean exUtilsLoaded = Loader.isModLoaded("ExtraUtilities");
        if (config.getBoolean("Compressed Dust", "blocks", true, "Set this to false to disable the recipe for the compressed dust.")) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, 0), "###", "###", "###", '#', GameRegistry.findBlock("exnihilo", "dust"));
            GameRegistry.addShapelessRecipe(new ItemStack(GameRegistry.findBlock("exnihilo", "dust"), 9), new ItemStack(ModBlocks.compressedBlock, 1, 0));
        }
        if (config.getBoolean("Compressed Cobblestone", "blocks", !exUtilsLoaded, "Set this to false to disable the recipe for the compressed cobblestone.")) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, 1), "###", "###", "###", '#', Blocks.cobblestone);
            GameRegistry.addShapelessRecipe(new ItemStack(Blocks.cobblestone, 9), new ItemStack(ModBlocks.compressedBlock, 1, 1));
        }
        if (config.getBoolean("Compressed Gravel", "blocks", !exUtilsLoaded, "Set this to false to disable the recipe for the compressed gravel.")) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, 2), "###", "###", "###", '#', Blocks.gravel);
            GameRegistry.addShapelessRecipe(new ItemStack(Blocks.gravel, 9), new ItemStack(ModBlocks.compressedBlock, 1, 2));
        }
        if (config.getBoolean("Compressed Sand", "blocks", !exUtilsLoaded, "Set this to false to disable the recipe for the compressed sand.")) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, 3), "###", "###", "###", '#', Blocks.sand);
            GameRegistry.addShapelessRecipe(new ItemStack(Blocks.sand, 9), new ItemStack(ModBlocks.compressedBlock, 1, 3));
        }
        if (config.getBoolean("Compressed Dirt", "blocks", !exUtilsLoaded, "Set this to false to disable the recipe for the compressed dirt.")) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, 4), "###", "###", "###", '#', Blocks.dirt);
            GameRegistry.addShapelessRecipe(new ItemStack(Blocks.dirt, 9), new ItemStack(ModBlocks.compressedBlock, 1, 4));
        }
        if (config.getBoolean("Compressed Flint", "blocks", true, "Set this to false to disable the recipe for the compressed flint.")) {
            GameRegistry.addRecipe(new ItemStack(ModBlocks.compressedBlock, 1, 5), "###", "###", "###", '#', Items.flint);
            GameRegistry.addShapelessRecipe(new ItemStack(Items.flint, 9), new ItemStack(ModBlocks.compressedBlock, 1, 5));
        }
    }
}
