package net.blay09.mods.excompressum.compat;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IAddon {
    void loadConfig(Configuration config);
    void postInit();
    void serverStarted(FMLServerStartedEvent event);
    @SideOnly(Side.CLIENT)
    void clientInit(); // thanks botania
}
