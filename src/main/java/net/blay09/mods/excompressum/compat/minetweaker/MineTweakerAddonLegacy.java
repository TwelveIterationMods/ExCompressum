package net.blay09.mods.excompressum.compat.minetweaker;

import cpw.mods.fml.common.event.FMLServerStartedEvent;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.registry.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("unused")
public class MineTweakerAddonLegacy implements IAddon {
    @Override
    public void loadConfig(Configuration config) {}

    @Override
    public void postInit() {}

    @Override
    public void serverStarted(FMLServerStartedEvent event) {
        HeavySieveRegistry.reload();
        CompressedRecipeRegistry.reload();
    }
}
