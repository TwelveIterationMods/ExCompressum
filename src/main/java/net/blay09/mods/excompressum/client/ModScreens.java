package net.blay09.mods.excompressum.client;

import net.blay09.mods.excompressum.client.gui.AutoCompressorScreen;
import net.blay09.mods.excompressum.client.gui.AutoHammerScreen;
import net.blay09.mods.excompressum.client.gui.AutoSieveScreen;
import net.blay09.mods.excompressum.client.gui.ManaSieveScreen;
import net.blay09.mods.excompressum.container.ModContainers;
import net.minecraft.client.gui.ScreenManager;

public class ModScreens {
    public static void register() {
        ScreenManager.registerFactory(ModContainers.autoCompressor, AutoCompressorScreen::new);
        ScreenManager.registerFactory(ModContainers.autoSieve, AutoSieveScreen::new);
        ScreenManager.registerFactory(ModContainers.autoHammer, AutoHammerScreen::new);
        ScreenManager.registerFactory(ModContainers.manaSieve, ManaSieveScreen::new);
    }
}
