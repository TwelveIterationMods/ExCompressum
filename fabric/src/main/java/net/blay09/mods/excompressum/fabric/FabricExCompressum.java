package net.blay09.mods.excompressum.fabric;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.blay09.mods.excompressum.ExCompressum;
import net.fabricmc.api.ModInitializer;

public class FabricExCompressum implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initializeMod(ExCompressum.MOD_ID, FabricLoadContext.INSTANCE, ExCompressum::initialize);
    }
}
