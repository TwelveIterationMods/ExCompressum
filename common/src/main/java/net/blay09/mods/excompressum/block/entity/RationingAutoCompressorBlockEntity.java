package net.blay09.mods.excompressum.block.entity;

import com.google.common.collect.Multiset;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RationingAutoCompressorBlockEntity extends AutoCompressorBlockEntity {
    public RationingAutoCompressorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.rationingAutoCompressor.get(), pos, state);
    }

    @Override
    public boolean shouldCompress(Multiset<CompressedRecipe> inputItems, CompressedRecipe compressedRecipe) {
        return inputItems.count(compressedRecipe) >= compressedRecipe.getCount() + 1;
    }
}
