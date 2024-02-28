package net.blay09.mods.excompressum.forge.compat.waila;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.*;

@WailaPlugin(id = ExCompressum.MOD_ID)
public class ExCompressumWailaPlugin implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.addComponent(new AutoSieveDataProvider(), TooltipPosition.BODY, AutoSieveBlock.class);
        registrar.addComponent(new AutoHammerDataProvider(), TooltipPosition.BODY, AutoHammerBlock.class);
        registrar.addComponent(new BaitDataProvider(), TooltipPosition.BODY, BaitBlock.class);
        registrar.addComponent(new WoodenCrucibleDataProvider(), TooltipPosition.BODY, WoodenCrucibleBlock.class);
        registrar.addComponent(new HeavySieveDataProvider(), TooltipPosition.BODY, HeavySieveBlock.class);
    }

}
