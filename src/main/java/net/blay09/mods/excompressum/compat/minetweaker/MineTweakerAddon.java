package net.blay09.mods.excompressum.compat.minetweaker;

import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.util.IEventHandler;
import net.blay09.mods.excompressum.compat.IAddon;
import net.blay09.mods.excompressum.registry.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;
import net.minecraftforge.common.config.Configuration;

@SuppressWarnings("unused")
@Optional.Interface(modid = "MineTweaker3", iface = "minetweaker.util.IEventHandler", striprefs = true)
public class MineTweakerAddon implements IEventHandler<MineTweakerImplementationAPI.ReloadEvent>, IAddon {
    public MineTweakerAddon() {
        MineTweakerImplementationAPI.onPostReload(this);
    }

    @Override
    public void handle(MineTweakerImplementationAPI.ReloadEvent reloadEvent) {
        HeavySieveRegistry.reload();
        CompressedRecipeRegistry.reload();
    }

    @Override
    public void loadConfig(Configuration config) {
    }

    @Override
    public void postInit() {
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {
    }
}
