package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;

public class EvolvedOrechidBlockEntity extends SubTileOrechid {

    public EvolvedOrechidBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.evolvedOrechid.get(), pos, state);
    }

    @Override
    public int getCost() {
        return ExCompressumConfig.getActive().compat.evolvedOrechidCost;
    }

    @Override
    public int getDelay() {
        return ExCompressumConfig.getActive().compat.evolvedOrechidDelay;
    }

}
