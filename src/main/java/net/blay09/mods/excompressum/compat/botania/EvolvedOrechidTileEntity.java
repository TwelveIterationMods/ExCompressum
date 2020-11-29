package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.blay09.mods.excompressum.tile.ModTileEntities;
import net.minecraft.tileentity.TileEntityType;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;

public class EvolvedOrechidTileEntity extends SubTileOrechid {

    public EvolvedOrechidTileEntity() {
        super(ModTileEntities.evolvedOrechid);
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
