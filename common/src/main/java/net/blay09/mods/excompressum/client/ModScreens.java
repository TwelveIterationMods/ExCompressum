package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.excompressum.client.gui.AutoCompressorScreen;
import net.blay09.mods.excompressum.client.gui.AutoHammerScreen;
import net.blay09.mods.excompressum.client.gui.AutoSieveScreen;
import net.blay09.mods.excompressum.menu.ModMenus;

import static net.blay09.mods.excompressum.ExCompressum.id;

public class ModScreens {
    public static void initialize(BalmScreens screens) {
        screens.registerScreen(id("auto_compressor"), ModMenus.autoCompressor::get, AutoCompressorScreen::new);
        screens.registerScreen(id("auto_sieve"), ModMenus.autoSieve::get, AutoSieveScreen::new);
        screens.registerScreen(id("auto_hammer"), ModMenus.autoHammer::get, AutoHammerScreen::new);
    }
}
