package net.blay09.mods.excompressum.compat;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

public interface IAddon {
    void loadConfig(Configuration config);
    void postInit();
    void serverStarted(FMLServerStartedEvent event);
}
