package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.excompressum.client.gui.AutoCompressorScreen;
import net.blay09.mods.excompressum.client.gui.AutoHammerScreen;
import net.blay09.mods.excompressum.client.gui.AutoSieveScreen;
import net.blay09.mods.excompressum.menu.ModMenus;

public class ModScreens {
    public static void initialize(BalmScreens screens) {
        screens.registerScreen(ModMenus.autoCompressor::get, AutoCompressorScreen::new);
        screens.registerScreen(ModMenus.autoSieve::get, AutoSieveScreen::new);
        screens.registerScreen(ModMenus.autoHammer::get, AutoHammerScreen::new);
    }
}
