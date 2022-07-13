package net.blay09.mods.excompressum.block;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.AutoHeavySieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.ManaSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AutoHeavySieveBlock extends AutoSieveBlock {

    public static final String name = "auto_heavy_sieve";
    public static final ResourceLocation registryName = new ResourceLocation(ExCompressum.MOD_ID, name);

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AutoHeavySieveBlockEntity(pos, state);
    }

    @Override
    public BlockEntityType<?> getBlockEntityType() {
        return ModBlockEntities.autoHeavySieve.get();
    }
}
