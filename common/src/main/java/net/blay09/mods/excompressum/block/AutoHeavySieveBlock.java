package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.block.entity.AutoHeavySieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class AutoHeavySieveBlock extends AutoSieveBlock {

    public AutoHeavySieveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoHeavySieveBlockEntity(pos, state);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return ModBlockEntities.autoHeavySieve.value();
    }

}
