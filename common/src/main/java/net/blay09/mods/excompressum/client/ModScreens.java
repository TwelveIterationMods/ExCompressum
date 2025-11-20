package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.client.gui.screens.inventory.BalmMenuScreenRegistrar;
import net.blay09.mods.excompressum.client.gui.AutoCompressorScreen;
import net.blay09.mods.excompressum.client.gui.AutoHammerScreen;
import net.blay09.mods.excompressum.client.gui.AutoSieveScreen;
import net.blay09.mods.excompressum.menu.ModMenus;

public class ModScreens {
    public static void initialize(BalmMenuScreenRegistrar screens) {
        screens.register(ModMenus.autoCompressor, AutoCompressorScreen::new);
        screens.register(ModMenus.autoSieve, AutoSieveScreen::new);
        screens.register(ModMenus.autoHammer, AutoHammerScreen::new);
    }
}
