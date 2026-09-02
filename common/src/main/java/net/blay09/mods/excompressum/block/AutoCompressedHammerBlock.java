package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.block.entity.AutoCompressedHammerBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import org.jspecify.annotations.Nullable;

public class AutoCompressedHammerBlock extends AutoHammerBlock {

    public AutoCompressedHammerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoCompressedHammerBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, UGLY);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide() ? createTickerHelper(type, ModBlockEntities.autoCompressedHammer.value(), AutoHammerBlockEntity::serverTick) : createTickerHelper(type, ModBlockEntities.autoCompressedHammer.value(), AutoHammerBlockEntity::clientTick);
    }

}
