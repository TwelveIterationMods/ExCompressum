package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;

public class EvolvedOrechidBlockEntity extends SubTileOrechid {

    public EvolvedOrechidBlockEntity() {
        super(ModBlockEntities.evolvedOrechid);
    }

    @Override
    public int getCost() {
        return ExCompressumConfig.COMMON.evolvedOrechidCost.get();
    }

    @Override
    public int getDelay() {
        return ExCompressumConfig.COMMON.evolvedOrechidDelay.get();
    }

}
