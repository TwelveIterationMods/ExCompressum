package net.blay09.mods.excompressum.compat.botania;

import net.blay09.mods.excompressum.config.ModConfig;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.subtile.functional.SubTileOrechid;

public class SubTileOrechidEvolved extends SubTileOrechid {

    @Override
    public int getCost() {
        return ModConfig.compat.evolvedOrechidCost;
    }

    @Override
    public int getDelay() {
        return ModConfig.compat.evolvedOrechidDelay;
    }

    @Override
    public LexiconEntry getEntry() {
        return BotaniaAddon.lexiconOrechidEvolved;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getBlockModel() { // NOTE This isn't actually used by Botania at all
        return BotaniaAPI.internalHandler.getSubTileBlockModelForName(BotaniaAPI.getSubTileStringMapping(SubTileOrechid.class));
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModelResourceLocation getItemModel() { // NOTE This isn't actually used by Botania at all
        return BotaniaAPI.internalHandler.getSubTileItemModelForName(BotaniaAPI.getSubTileStringMapping(SubTileOrechid.class));
    }
}
