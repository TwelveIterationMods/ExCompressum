package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.blay09.mods.excompressum.block.*;

public class WailaProvider {
    public static void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new AutoSieveDataProvider(), BlockAutoSieveBase.class);
        registrar.registerBodyProvider(new BaitDataProvider(), BlockBait.class);
        registrar.registerBodyProvider(new WoodenCrucibleDataProvider(), BlockWoodenCrucible.class);
        registrar.registerBodyProvider(new HeavySieveDataProvider(), BlockHeavySieve.class);
    }
}
