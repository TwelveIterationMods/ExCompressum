package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tile.AutoHammerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;

public class AutoHammerBlock extends ContainerBlock implements IUglyfiable {

    public static final String name = "auto_hammer";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty UGLY = BooleanProperty.create("ugly");

    public AutoHammerBlock() {
        super(Properties.create(Material.IRON).hardnessAndResistance(2f));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, UGLY);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new AutoHammerTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!player.isSneaking() && !world.isRemote) {
            // TODO NetworkHooks.openGui(player, null, pos);
        }

        return ActionResultType.SUCCESS;
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
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        Direction facing = context.getPlacementHorizontalFacing();
        return getDefaultState().with(FACING, facing);
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
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
        if (tileEntity != null) {
            final IItemHandler itemHandler = tileEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).resolve().orElse(null);
            return ItemHandlerHelper.calcRedstoneFromInventory(itemHandler);
        } else {
            return 0;
        }
    }

    @Override
    public boolean uglify(@Nullable PlayerEntity player, World world, BlockPos pos, BlockState state, Hand hand, Direction facing, Vector3d hitVec) {
        if (!state.get(UGLY)) {
            world.setBlockState(pos, state.with(UGLY, true), 3);
            return true;
        }

        return false;
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean isMoving) {
        updateRedstoneState(world, pos);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        updateRedstoneState(world, pos);
    }

    private void updateRedstoneState(World world, BlockPos pos) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof AutoHammerTileEntity) {
            ((AutoHammerTileEntity) tileEntity).setDisabledByRedstone(world.isBlockPowered(pos));
        }
    }
}
