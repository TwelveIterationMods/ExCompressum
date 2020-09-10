package net.blay09.mods.excompressum.tile;

import com.google.common.collect.Multiset;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipe;

public class RationingAutoCompressorTileEntity extends AutoCompressorTileEntity {
    public RationingAutoCompressorTileEntity() {
        super(ModTileEntities.rationingAutoCompressor);
    }

    @Override
    public boolean shouldCompress(Multiset<CompressedRecipe> inputItems, CompressedRecipe compressedRecipe) {
        return inputItems.count(compressedRecipe) >= compressedRecipe.getCount() + 1;
    }
}
