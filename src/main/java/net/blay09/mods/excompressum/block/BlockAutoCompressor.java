package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.tile.TileAutoCompressor;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class BlockAutoCompressor extends BlockContainer {

    public BlockAutoCompressor() {
        super(Material.IRON);
        setCreativeTab(ExCompressum.creativeTab);
        setHardness(2f);
        setRegistryName("auto_compressor");
        setUnlocalizedName(getRegistryName().toString());
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileAutoCompressor();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking() && !world.isRemote) {
            player.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_COMPRESSOR, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity != null) {
            IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));
                }
            }
            ItemStack currentStack = ((TileAutoCompressor) tileEntity).getCurrentStack();
            if (!currentStack.isEmpty()) {
                world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), currentStack));
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound != null && tagCompound.hasKey("EnergyStored")) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof TileAutoCompressor) {
                ((TileAutoCompressor) tileEntity).getEnergyStorage().setEnergyStored(tagCompound.getInteger("EnergyStored"));
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null ? ItemHandlerHelper.calcRedstoneFromInventory(tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) : 0;
    }

}
