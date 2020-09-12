package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.tile.WoodenCrucibleTileEntity;
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
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidUtil;

public class WoodenCrucibleBlock extends ContainerBlock {

    public static final String namePrefix = "wooden_crucible_";
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
                    return ActionResultType.SUCCESS;
                }
            }

            FluidUtil.interactWithFluidHandler(player, hand, world, pos, hit.getFace());
        }

        return ActionResultType.SUCCESS;
    }

}
