package net.blay09.mods.excompressum.block;

import cofh.api.block.IDismantleable;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BlockAutoCompressedHammer extends BlockContainer implements IDismantleable {

    public BlockAutoCompressedHammer() {
        super(Material.iron);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setBlockName(ExCompressum.MOD_ID + ":autoCompressedHammer");
        setBlockTextureName(ExCompressum.MOD_ID + ":autoCompressedHammer");
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int Z, EntityPlayer entityPlayer, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public int getRenderType() {
        return 0;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        IInventory tileEntity = (IInventory) world.getTileEntity(x, y, z);
        for (int i = 0; i < tileEntity.getSizeInventory(); i++) {
            if (tileEntity.getStackInSlot(i) != null) {
                EntityItem entityItem = new EntityItem(world, x, y, z, tileEntity.getStackInSlot(i));
                world.spawnEntityInWorld(entityItem);
            }
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
}
