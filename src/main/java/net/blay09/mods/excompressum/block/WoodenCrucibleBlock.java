package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.tile.WoodenCrucibleTileEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class WoodenCrucibleBlock extends ContainerBlock {

    public static final String nameSuffix = "_crucible";

    private static final VoxelShape BOUNDING_BOX = VoxelShapes.or(
            VoxelShapes.create(0, 0.1875f, 0, 1, 1f, 1),
            VoxelShapes.create(0.0625f, 0.125f, 0.0625f, 1 - 0.0625f, 0.1875f, 1 - 0.0625f),
            VoxelShapes.create(0.125f, 0.0625f, 0.125f, 1 - 0.125f, 0.125f, 1 - 0.125f),
            VoxelShapes.create(0, 0, 0, 0.0625f, 0.1875f, 0.0625f),
            VoxelShapes.create(1 - 0.0625f, 0, 0, 1, 0.1875f, 0.0625f),
            VoxelShapes.create(0, 0, 1 - 0.0625f, 0.0625f, 0.1875f, 1),
            VoxelShapes.create(1 - 0.0625f, 0, 1 - 0.0625f, 1, 0.1875f, 1)
    ).simplify();

    private final WoodenCrucibleType type;

    public WoodenCrucibleBlock(WoodenCrucibleType type) {
        super(Properties.create(Material.WOOD).hardnessAndResistance(2f));
        this.type = type;
    }

    @Override
    public TileEntity createNewTileEntity(IBlockReader world) {
        return new WoodenCrucibleTileEntity();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        WoodenCrucibleTileEntity tileEntity = (WoodenCrucibleTileEntity) world.getTileEntity(pos);
        if (tileEntity != null) {
            ItemStack heldItem = player.getHeldItem(hand);
            ItemStack outputStack = tileEntity.getItemHandler().extractItem(0, 64, false);
            if (!outputStack.isEmpty()) {
                if (!player.inventory.addItemStackToInventory(outputStack)) {
                    world.addEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, outputStack));
                }

                return ActionResultType.SUCCESS;
            }

            if (!heldItem.isEmpty()) {
                if (tileEntity.addItem(heldItem, false, false)) {
                    if (!player.abilities.isCreativeMode) {
                        heldItem.shrink(1);
                    }
                    return ActionResultType.SUCCESS;
                }
            }

            FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace());
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return BOUNDING_BOX;
    }
}
