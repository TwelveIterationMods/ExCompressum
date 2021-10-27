package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.block.entity.WoodenCrucibleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class WoodenCrucibleBlock extends BaseEntityBlock {

    public static final String nameSuffix = "_crucible";

    private static final VoxelShape BOUNDING_BOX = Shapes.or(
            box(0, 0.1875f, 0, 1, 1f, 1),
            box(0.0625f, 0.125f, 0.0625f, 1 - 0.0625f, 0.1875f, 1 - 0.0625f),
            box(0.125f, 0.0625f, 0.125f, 1 - 0.125f, 0.125f, 1 - 0.125f),
            box(0, 0, 0, 0.0625f, 0.1875f, 0.0625f),
            box(1 - 0.0625f, 0, 0, 1, 0.1875f, 0.0625f),
            box(0, 0, 1 - 0.0625f, 0.0625f, 0.1875f, 1),
            box(1 - 0.0625f, 0, 1 - 0.0625f, 1, 0.1875f, 1)
    ).optimize();

    private final WoodenCrucibleType type;

    public WoodenCrucibleBlock(WoodenCrucibleType type) {
        super(Properties.of(Material.WOOD).strength(2f));
        this.type = type;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WoodenCrucibleBlockEntity(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.getBlockEntity(pos) instanceof WoodenCrucibleBlockEntity woodenCrucible) {
            ItemStack heldItem = player.getItemInHand(hand);
            ItemStack outputStack = woodenCrucible.getItemHandler().extractItem(0, 64, false);
            if (!outputStack.isEmpty()) {
                if (!player.getInventory().add(outputStack)) {
                    level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, outputStack));
                }

                return InteractionResult.SUCCESS;
            }

            if (!heldItem.isEmpty()) {
                if (woodenCrucible.addItem(heldItem, false, false)) {
                    if (!player.getAbilities().instabuild) {
                        heldItem.shrink(1);
                    }
                    return InteractionResult.SUCCESS;
                }
            }

            FluidUtil.interactWithFluidHandler(player, hand, level, pos, hit.getDirection());
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
        return BOUNDING_BOX;
    }
}
