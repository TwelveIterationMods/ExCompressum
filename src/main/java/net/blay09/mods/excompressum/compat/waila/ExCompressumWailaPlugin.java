package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;

@WailaPlugin(ExCompressum.MOD_ID)
public class ExCompressumWailaPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(new AutoSieveDataProvider(), TooltipPosition.BODY, AutoSieveBlock.class);
        registrar.registerComponentProvider(new AutoHammerDataProvider(), TooltipPosition.BODY, AutoHammerBlock.class);
        registrar.registerComponentProvider(new BaitDataProvider(), TooltipPosition.BODY, BaitBlock.class);
        registrar.registerComponentProvider(new WoodenCrucibleDataProvider(), TooltipPosition.BODY, WoodenCrucibleBlock.class);
        registrar.registerComponentProvider(new HeavySieveDataProvider(), TooltipPosition.BODY, HeavySieveBlock.class);
    }

}
