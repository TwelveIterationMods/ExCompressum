package net.blay09.mods.excompressum.block;

import cofh.api.block.IDismantleable;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;

public class BlockAutoCompressor extends BlockContainer implements IDismantleable {

    private IIcon iconTop;
    private IIcon iconSide;

    public BlockAutoCompressor() {
        super(Material.iron);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setBlockName(ExCompressum.MOD_ID + ":auto_compressor");
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        iconTop = iconRegister.registerIcon(ExCompressum.MOD_ID + ":auto_compressor_top");
        iconSide = iconRegister.registerIcon(ExCompressum.MOD_ID + ":auto_compressor_side");
    }

    @Override
    public IIcon getIcon(int side, int metadata) {
        if(side == ForgeDirection.UP.ordinal()) {
            return iconTop;
        }
        return iconSide;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoCompressor();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        if(!entityPlayer.isSneaking()) {
            entityPlayer.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_COMPRESSOR, world, x, y, z);
        }
        return true;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        IInventory tileEntity = (IInventory) world.getTileEntity(x, y, z);
        for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
            if (tileEntity.getStackInSlot(i) != null) {
                EntityItem entityItem = new EntityItem(world, x, y, z, tileEntity.getStackInSlot(i));
                double motion = 0.05;
                entityItem.motionX = world.rand.nextGaussian() * motion;
                entityItem.motionY = 0.2;
                entityItem.motionZ = world.rand.nextGaussian() * motion;
                world.spawnEntityInWorld(entityItem);
            }
        }
        if(((TileEntityAutoCompressor) tileEntity).getCurrentStack() != null) {
            EntityItem entityItem = new EntityItem(world, x, y, z, ((TileEntityAutoCompressor) tileEntity).getCurrentStack());
            double motion = 0.05;
            entityItem.motionX = world.rand.nextGaussian() * motion;
            entityItem.motionY = 0.2;
            entityItem.motionZ = world.rand.nextGaussian() * motion;
            world.spawnEntityInWorld(entityItem);
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack stack) {
        if (stack.stackTagCompound != null && stack.stackTagCompound.hasKey("EnergyStored")) {
            TileEntityAutoCompressor tileEntity = (TileEntityAutoCompressor) world.getTileEntity(x, y, z);
            tileEntity.setEnergyStored(stack.stackTagCompound.getInteger("EnergyStored"));
        }
        super.onBlockPlacedBy(world, x, y, z, player, stack);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer entityPlayer, World world, int x, int y, int z, boolean returnDrops) {
        TileEntityAutoCompressor tileEntity = (TileEntityAutoCompressor) world.getTileEntity(x, y, z);
        ItemStack itemStack = new ItemStack(this);
        if (itemStack.stackTagCompound == null) {
            itemStack.stackTagCompound = new NBTTagCompound();
        }
        itemStack.stackTagCompound.setInteger("EnergyStored", tileEntity.getEnergyStored(null));

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
        if(ModAPIManager.INSTANCE.hasAPI("CoFHAPI")) {
            if (config.getBoolean("Auto Compressor", "blocks", true, "Set this to false to disable the recipe for the auto compressor.")) {
                GameRegistry.addRecipe(new ItemStack(ModBlocks.autoCompressor), "#I#", "IBI", "#I#", '#', Blocks.crafting_table, 'B', Blocks.iron_block, 'I', Items.iron_ingot);
            }
        }
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
        return Container.calcRedstoneFromInventory((IInventory) world.getTileEntity(x, y, z));
    }
}
