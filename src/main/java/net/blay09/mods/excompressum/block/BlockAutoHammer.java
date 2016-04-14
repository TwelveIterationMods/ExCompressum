package net.blay09.mods.excompressum.block;

import cofh.api.block.IDismantleable;
import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModAPIManager;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.tile.TileEntityAutoHammer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.ArrayList;

public class BlockAutoHammer extends BlockContainer implements IDismantleable {

    public static final IIcon[] destroyStages = new IIcon[10];

    public BlockAutoHammer() {
        super(Material.iron);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setBlockName(ExCompressum.MOD_ID + ":auto_hammer");
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        for (int i = 0; i < destroyStages.length; i++) {
            destroyStages[i] = iconRegister.registerIcon("destroy_stage_" + i);
        }
    }

    @Override
    public int getRenderType() {
        return -1;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileEntityAutoHammer();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        if(!entityPlayer.isSneaking()) {
            entityPlayer.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_HAMMER, world, x, y, z);
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
        if (((TileEntityAutoHammer) tileEntity).getCurrentStack() != null) {
            EntityItem entityItem = new EntityItem(world, x, y, z, ((TileEntityAutoHammer) tileEntity).getCurrentStack());
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
            TileEntityAutoHammer tileEntity = (TileEntityAutoHammer) world.getTileEntity(x, y, z);
            tileEntity.setEnergyStored(stack.stackTagCompound.getInteger("EnergyStored"));
        }
        int facing = MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        if (facing == 0) {
            world.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }
        if (facing == 1) {
            world.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }
        if (facing == 2) {
            world.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }
        if (facing == 3) {
            world.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }
        super.onBlockPlacedBy(world, x, y, z, player, stack);
    }

    @Override
    public ArrayList<ItemStack> dismantleBlock(EntityPlayer entityPlayer, World world, int x, int y, int z, boolean returnDrops) {
        TileEntityAutoHammer tileEntity = (TileEntityAutoHammer) world.getTileEntity(x, y, z);
        ItemStack itemStack = new ItemStack(this);
        if (itemStack.stackTagCompound == null) {
            itemStack.stackTagCompound = new NBTTagCompound();
        }
        itemStack.stackTagCompound.setInteger("EnergyStored", tileEntity.getEnergyStored(null));

        ArrayList<ItemStack> drops = Lists.newArrayList();
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
            if (config.getBoolean("Auto Hammer", "blocks", true, "Set this to false to disable the recipe for the auto hammer.")) {
                Item hammerDiamond = GameRegistry.findItem("exnihilo", "hammer_diamond");
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.autoHammer), "IPI", "IHI", "IPI", 'P', Blocks.heavy_weighted_pressure_plate, 'H', hammerDiamond, 'I', "ingotIron"));
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
