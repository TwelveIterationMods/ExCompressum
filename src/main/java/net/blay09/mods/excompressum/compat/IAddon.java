package net.blay09.mods.excompressum.compat;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.common.config.Configuration;

public interface IAddon {
    void loadConfig(Configuration config);
    void postInit();
    void serverStarted(FMLServerStartedEvent event);
}
