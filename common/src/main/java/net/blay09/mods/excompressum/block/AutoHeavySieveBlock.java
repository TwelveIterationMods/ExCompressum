package net.blay09.mods.excompressum.block;

import com.mojang.serialization.MapCodec;
import net.blay09.mods.excompressum.block.entity.AutoHeavySieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AutoHeavySieveBlock extends AutoSieveBlock {

    public static final MapCodec<AutoHeavySieveBlock> CODEC = simpleCodec(AutoHeavySieveBlock::new);

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

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
