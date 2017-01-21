package net.blay09.mods.excompressum.compat;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// TODO register all of these in preInit instead of special-casing botania
public interface IAddon {
    void loadConfig(Configuration config);
    void init();
    void postInit();
    void serverStarted(FMLServerStartedEvent event);
    @SideOnly(Side.CLIENT)
    void clientInit(); // thanks botania
}
