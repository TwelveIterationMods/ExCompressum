package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.util.IIcon;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;
import vazkii.botania.common.lib.LibBlockNames;

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

    @Override
    public IIcon getIcon() {
        return BotaniaAPI.internalHandler.getSubTileIconForName(LibBlockNames.SUBTILE_ORECHID);
    }

}
