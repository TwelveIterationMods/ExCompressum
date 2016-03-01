package net.blay09.mods.excompressum.compat.exastris;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.blay09.mods.excompressum.compat.IAddon;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("unused")
public class ExAstrisAddon implements IAddon {

    public static Item sieveUpgrade;

    @Override
    public void loadConfig(Configuration config) {

    }

    @Override
    public void postInit() {
        sieveUpgrade = GameRegistry.findItem("exastris", "");
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {}

}
