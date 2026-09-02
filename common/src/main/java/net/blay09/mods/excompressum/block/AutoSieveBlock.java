package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.block.entity.AutoSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;

import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;
import org.jspecify.annotations.Nullable;

public class AutoSieveBlock extends AutoSieveBaseBlock {

    public AutoSieveBlock(Properties properties) {
        super(properties
                .noOcclusion()
                .isValidSpawn((a, b, c, d) -> false)
                .isRedstoneConductor((a, b, c) -> false)
                .isSuffocating((a, b, c) -> false)
                .isViewBlocking((a, b, c) -> false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoSieveBlockEntity(pos, state);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return ModBlockEntities.autoSieve.value();
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        updateRedstoneState(level, pos);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
        updateRedstoneState(level, pos);
    }

    private void updateRedstoneState(Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof AbstractAutoSieveBlockEntity autoSieve) {
            autoSieve.setDisabledByRedstone(level.hasNeighborSignal(pos));
        }
    }

}
