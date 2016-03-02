package net.blay09.mods.excompressum.block;

import cofh.api.block.IDismantleable;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import java.util.ArrayList;

public class BlockAutoCompressedHammer extends BlockContainer implements IDismantleable {

    public BlockAutoCompressedHammer() {
        super(Material.iron);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setBlockName(ExCompressum.MOD_ID + ":auto_compressed_hammer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoCompressedHammer();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        entityPlayer.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_COMPRESSED_HAMMER, world, x, y, z);
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
        if(((TileEntityAutoCompressedHammer) tileEntity).getCurrentStack() != null) {
            EntityItem entityItem = new EntityItem(world, x, y, z, ((TileEntityAutoCompressedHammer) tileEntity).getCurrentStack());
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
            TileEntityAutoCompressedHammer tileEntity = (TileEntityAutoCompressedHammer) world.getTileEntity(x, y, z);
            tileEntity.setEnergyStored(stack.stackTagCompound.getInteger("EnergyStored"));
        }
        super.onBlockPlacedBy(world, x, y, z, player, stack);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer entityPlayer, World world, int x, int y, int z, boolean returnDrops) {
        TileEntityAutoCompressedHammer tileEntity = (TileEntityAutoCompressedHammer) world.getTileEntity(x, y, z);
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
        if(Loader.isModLoaded("CoFHCore")) {
            if (config.getBoolean("Auto Compressed Hammer", "blocks", true, "Set this to false to disable the recipe for the auto compressed hammer.")) {
                GameRegistry.addRecipe(new ItemStack(ModBlocks.autoCompressedHammer), "BPB", "HHH", "BPB", 'P', Blocks.heavy_weighted_pressure_plate, 'H', ModItems.compressedHammerDiamond, 'B', Blocks.iron_block);
            }
        }
    }
}
