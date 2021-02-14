package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.registry.SieveModelBounds;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.tile.HeavySieveTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.items.ItemHandlerHelper;

public class HeavySieveBlock extends ContainerBlock {

    public static final String nameSuffix = "_heavy_sieve";

    public static final SieveModelBounds SIEVE_BOUNDS = new SieveModelBounds(0.5625f, 0.0625f, 0.88f, 0.5f);

    private static final VoxelShape BOUNDING_BOX = VoxelShapes.or(
            VoxelShapes.create(0, 0.5f, 0, 1, 0.75f, 1),
            VoxelShapes.create(0.0625f, 0, 0.0625f, 0.125f, 0.5, 0.125f),
            VoxelShapes.create(0.0625f, 0, 1 - 0.0625f, 0.125f, 0.5, 1 - 0.125f),
            VoxelShapes.create(1 - 0.0625f, 0, 0.0625f, 1 - 0.125f, 0.5, 0.125f),
            VoxelShapes.create(1 - 0.0625f, 0, 1 - 0.0625f, 1 - 0.125f, 0.5, 1 - 0.125f)
    ).simplify();

    public static final BooleanProperty WITH_MESH = BooleanProperty.create("with_mesh");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final HeavySieveType type;

    public HeavySieveBlock(HeavySieveType type) {
        super(Properties.create(Material.WOOD).hardnessAndResistance(2f));
        setDefaultState(getDefaultState().with(WATERLOGGED, false));
        this.type = type;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDING_BOX;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(WITH_MESH, WATERLOGGED);
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new HeavySieveTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        HeavySieveTileEntity tileEntity = (HeavySieveTileEntity) world.getTileEntity(pos);
        if (tileEntity != null) {
            ItemStack heldItem = player.getHeldItem(hand);
            if (!heldItem.isEmpty()) {
                SieveMeshRegistryEntry sieveMesh = SieveMeshRegistry.getEntry(heldItem);
                if (sieveMesh != null && tileEntity.getMeshStack().isEmpty()) {
                    tileEntity.setMeshStack(player.abilities.isCreativeMode ? ItemHandlerHelper.copyStackWithSize(heldItem, 1) : heldItem.split(1));
                    return ActionResultType.SUCCESS;
                }

                if (tileEntity.addSiftable(player, heldItem)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_GRAVEL_STEP, SoundCategory.BLOCKS, 0.5f, 1f);
                    return ActionResultType.SUCCESS;
                }
            } else {
                if (!world.isRemote && player.isSneaking()) {
                    ItemStack meshStack = tileEntity.getMeshStack();
                    if (!meshStack.isEmpty() && tileEntity.getCurrentStack().isEmpty()) {
                        if (player.inventory.addItemStackToInventory(meshStack)) {
                            world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, meshStack));
                        }
                        tileEntity.setMeshStack(ItemStack.EMPTY);
                    }
                }
            }

            if (ExCompressumConfig.COMMON.allowHeavySieveAutomation.get() || !(player instanceof FakePlayer)) {
                if (tileEntity.processContents(player)) {
                    world.playSound(null, pos, SoundEvents.BLOCK_SAND_STEP, SoundCategory.BLOCKS, 0.3f, 0.6f);
                }
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof HeavySieveTileEntity) {
            HeavySieveTileEntity tileHeavySieve = (HeavySieveTileEntity) tileEntity;
            if (!tileHeavySieve.getMeshStack().isEmpty()) {
                world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, tileHeavySieve.getMeshStack()));
            }
        }
        super.onReplaced(state, world, pos, newState, isMoving);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        FluidState fluidState = context.getWorld().getFluidState(context.getPos());
        return this.getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.get(WATERLOGGED)) {
            worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
        }
        return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStillFluidState(false) : super.getFluidState(state);
    }
}
