package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.handler.GuiHandler;
import net.blay09.mods.excompressum.tile.AutoCompressorTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class AutoCompressorBlock extends ContainerBlock {

    public static final String name = "auto_compressor";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public AutoCompressorBlock() {
        super(Material.IRON);
        setCreativeTab(ExCompressum.itemGroup);
        setHardness(2f);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new AutoCompressorTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, BlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if(!player.isSneaking() && !world.isRemote) {
            player.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_COMPRESSOR, world, pos.getX(), pos.getY(), pos.getZ());
        }
        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity != null) {
            IItemHandler itemHandler = ((AutoCompressorTileEntity) tileEntity).getItemHandler();
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));
                }
            }
            for(ItemStack currentStack : ((AutoCompressorTileEntity) tileEntity).getCurrentBuffer()) {
                if (!currentStack.isEmpty()) {
                    world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), currentStack));
                }
            }
        }
        super.breakBlock(world, pos, state);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound != null && tagCompound.contains("EnergyStored")) {
            TileEntity tileEntity = world.getTileEntity(pos);
            if(tileEntity instanceof AutoCompressorTileEntity) {
                ((AutoCompressorTileEntity) tileEntity).getEnergyStorage().setEnergyStored(tagCompound.getInt("EnergyStored"));
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getComparatorInputOverride(BlockState blockState, World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        return tileEntity != null ? ItemHandlerHelper.calcRedstoneFromInventory(tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) : 0;
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, BlockState state) {
        updateRedstoneState(world, pos);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        updateRedstoneState(world, pos);
    }

    private void updateRedstoneState(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if(tileEntity instanceof AutoCompressorTileEntity) {
            ((AutoCompressorTileEntity) tileEntity).setDisabledByRedstone(world.isBlockPowered(pos));
        }
    }
}
