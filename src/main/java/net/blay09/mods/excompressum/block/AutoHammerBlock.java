package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tile.AutoHammerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class AutoHammerBlock extends ContainerBlock implements IUglyfiable {

    public static final String name = "auto_hammer";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty UGLY = BooleanProperty.create("ugly");

    public AutoHammerBlock() {
        super(Material.IRON);
        setCreativeTab(ExCompressum.itemGroup);
        setHardness(2f);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, UGLY);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new AutoHammerTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking() && !world.isRemote) {
            player.openGui(ExCompressum.instance, GuiHandler.GUI_AUTO_HAMMER, world, pos.getX(), pos.getY(), pos.getZ());
        }

        return true;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, BlockState state) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null) {
            IItemHandler itemHandler = ((AutoHammerTileEntity) tileEntity).getItemHandler();
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                ItemStack itemStack = itemHandler.getStackInSlot(i);
                if (!itemStack.isEmpty()) {
                    world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack));
                }
            }
            ItemStack currentStack = ((AutoHammerTileEntity) tileEntity).getCurrentStack();
            if (!currentStack.isEmpty()) {
                world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), currentStack));
            }
        }
        if (state.get(UGLY)) {
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ModItems.uglySteelPlating)));
        }

        super.breakBlock(world, pos, state);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        EnumFacing facing = EnumFacing.getDirectionFromEntityLiving(pos, placer);
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = EnumFacing.NORTH;
        }
        return getStateFromMeta(meta).withProperty(FACING, facing);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, EntityLivingBase placer, ItemStack stack) {
        CompoundNBT tagCompound = stack.getTag();
        if (tagCompound != null && tagCompound.contains("EnergyStored")) {
            AutoHammerTileEntity tileEntity = (AutoHammerTileEntity) world.getTileEntity(pos);
            if (tileEntity != null) {
                tileEntity.getEnergyStorage().setEnergyStored(tagCompound.getInt("EnergyStored"));
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
    public boolean uglify(PlayerEntity player, World world, BlockPos pos, BlockState state, Hand hand, Direction facing, Vector3d hitVec) {
        if (!state.get(UGLY)) {
            world.setBlockState(pos, state.with(UGLY, true), 3);
            return true;
        }
        return false;
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
        if (tileEntity instanceof AutoHammerTileEntity) {
            ((AutoHammerTileEntity) tileEntity).setDisabledByRedstone(world.isBlockPowered(pos));
        }
    }
}
