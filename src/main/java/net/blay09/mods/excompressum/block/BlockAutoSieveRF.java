package net.blay09.mods.excompressum.block;

import cofh.api.block.IDismantleable;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import exnihilo.blocks.BlockSieve;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.registry.AutoSieveSkinRegistry;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieveRF;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemFood;
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

@Optional.Interface(modid = "CoFHCore", iface = "cofh.api.block.IDismantleable", striprefs = true)
public class BlockAutoSieveRF extends BlockContainer implements IDismantleable {

    public BlockAutoSieveRF() {
        super(Material.iron);
        setBlockName(ExCompressum.MOD_ID + ":auto_sieve");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoSieveRF();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack itemStack) {
        TileEntityAutoSieveRF tileEntity = (TileEntityAutoSieveRF) world.getTileEntity(x, y, z);
        if (itemStack.stackTagCompound != null) {
            if (itemStack.stackTagCompound.hasKey("EnergyStored")) {
                tileEntity.setEnergyStored(itemStack.stackTagCompound.getInteger("EnergyStored"));
            }
        }
        super.onBlockPlacedBy(world, x, y, z, player, itemStack);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer entityPlayer, World world, int x, int y, int z, boolean returnDrops) {
        TileEntityAutoSieveRF tileEntity = (TileEntityAutoSieveRF) world.getTileEntity(x, y, z);
        ItemStack itemStack = new ItemStack(this);
        if (itemStack.stackTagCompound == null) {
            itemStack.stackTagCompound = new NBTTagCompound();
        }
        itemStack.stackTagCompound.setInteger("EnergyStored", tileEntity.getEnergyStored(null));
        NBTTagCompound customSkinTag = new NBTTagCompound();
        NBTUtil.func_152460_a(customSkinTag, tileEntity.getCustomSkin());
        itemStack.stackTagCompound.setTag("CustomSkin", customSkinTag);

        ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(itemStack);
        world.setBlockToAir(x, y, z);
        if (!returnDrops) {
            dropBlockAsItem(world, x, y, z, itemStack);
        }
        return drops;
    }

    @Override
    public boolean canDismantle(EntityPlayer entityPlayer, World world, int x, int y, int z) {
        return true;
    }

    public static void registerRecipes(Configuration config) {
        if (Loader.isModLoaded("CoFHCore")) {
            if (config.getBoolean("Auto Sieve", "blocks", true, "Set this to false to disable the recipe for the auto sieve.")) {
                ItemStack sieve = new ItemStack(GameRegistry.findBlock("exnihilo", "sifting_table"), 1, OreDictionary.WILDCARD_VALUE);
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoSieve), "BGB", "GSG", "IGI", 'B', "blockIron", 'S', sieve, 'G', "paneGlassColorless", 'I', "ingotIron"));
            }
        }
    }
}
