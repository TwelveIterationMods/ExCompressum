package net.blay09.mods.excompressum.compat.botania;

import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;

public class SubTileOrechidEvolved extends SubTileOrechid {

    @Override
    public int getCost() {
        return BotaniaAddon.evolvedOrechidCost;
    }

    @Override
    public int getDelay() {
        return BotaniaAddon.evolvedOrechidDelay;
    }

    @Override
    public LexiconEntry getEntry() {
        return BotaniaAddon.lexiconOrechidEvolved;
    }

}
