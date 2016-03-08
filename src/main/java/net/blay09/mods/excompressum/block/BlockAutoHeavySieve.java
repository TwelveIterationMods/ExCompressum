package net.blay09.mods.excompressum.block;

import cofh.api.block.IDismantleable;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.tile.TileEntityAutoHeavySieve;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;

public class BlockAutoHeavySieve extends BlockAutoSieve implements IDismantleable {

    public BlockAutoHeavySieve() {
        setBlockName(ExCompressum.MOD_ID + ":auto_heavy_sieve");
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        return BlockHeavySieve.meshIcon;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoHeavySieve();
    }

    public static void registerRecipes(Configuration config) {
        if (Loader.isModLoaded("CoFHCore")) {
            if (config.getBoolean("Auto Heavy Sieve", "blocks", true, "Set this to false to disable the recipe for the auto heavy sieve.")) {
                if(OreDictionary.getOres("blockSteel", false).isEmpty() || OreDictionary.getOres("ingotSteel", false).isEmpty()) {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHeavySieve), "BGB", "GSG", "IGI", 'B', "blockIron", 'S', new ItemStack(ModBlocks.heavySieve, 1, OreDictionary.WILDCARD_VALUE), 'G', "paneGlass", 'I', "ingotSteel"));
                } else {
                    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHeavySieve), "BGB", "GSG", "IGI", 'B', "blockSteel", 'S', new ItemStack(ModBlocks.heavySieve, 1, OreDictionary.WILDCARD_VALUE), 'G', "paneGlass", 'I', "ingotSteel"));
                }
            }
        }
    }
}
