package net.blay09.mods.excompressum.fabric;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.excompressum.ExCompressum;
import net.fabricmc.api.ModInitializer;

public class FabricExCompressum implements ModInitializer {
    @Override
    public void onInitialize() {
        Balm.initialize(ExCompressum.MOD_ID, ExCompressum::initialize);
    }
}
