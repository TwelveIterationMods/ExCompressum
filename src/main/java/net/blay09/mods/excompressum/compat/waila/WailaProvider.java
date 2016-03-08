package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.blay09.mods.excompressum.block.BlockAutoHeavySieve;
import net.blay09.mods.excompressum.block.BlockBait;

public class WailaProvider {
    public static void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new AutoHeavySieveDataProvider(), BlockAutoHeavySieve.class);
        registrar.registerBodyProvider(new BaitDataProvider(), BlockBait.class);
    }
}
