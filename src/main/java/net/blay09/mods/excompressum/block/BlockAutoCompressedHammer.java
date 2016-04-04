package net.blay09.mods.excompressum.block;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;

public class BlockAutoCompressedHammer extends BlockAutoHammer {

    public BlockAutoCompressedHammer() {
        setBlockName(ExCompressum.MOD_ID + ":auto_compressed_hammer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoCompressedHammer();
    }

    public static void registerRecipes(Configuration config) {
        if (Loader.isModLoaded("CoFHCore")) {
            if (config.getBoolean("Auto Compressed Hammer", "blocks", true, "Set this to false to disable the recipe for the auto compressed hammer.")) {
                if(OreDictionary.getOres("blockSteel", false).isEmpty() || OreDictionary.getOres("ingotSteel", false).isEmpty()) {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "BPB", "IHI", "BPB", 'P', Blocks.heavy_weighted_pressure_plate, 'H', ModItems.compressedHammerDiamond, 'B', "blockIron", 'I', "ingotIron"));
                } else {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "IPI", "IHI", "IPI", 'P', Blocks.heavy_weighted_pressure_plate, 'H', ModItems.compressedHammerDiamond, 'B', "blockSteel", 'I', "ingotSteel"));
                }
            }
        }
    }
}
