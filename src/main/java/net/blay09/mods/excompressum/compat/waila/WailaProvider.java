package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.blay09.mods.excompressum.block.BlockAutoSieve;
import net.blay09.mods.excompressum.block.BlockBait;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.blay09.mods.excompressum.block.BlockWoodenCrucible;

public class WailaProvider {
    public static void register(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new AutoSieveDataProvider(), BlockAutoSieve.class);
        registrar.registerBodyProvider(new BaitDataProvider(), BlockBait.class);
        registrar.registerBodyProvider(new WoodenCrucibleDataProvider(), BlockWoodenCrucible.class);
        registrar.registerBodyProvider(new HeavySieveDataProvider(), BlockHeavySieve.class);
    }
}
