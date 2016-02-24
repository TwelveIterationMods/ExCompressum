package net.blay09.mods.excompressum.compat;

import cpw.mods.fml.common.Optional;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.util.IEventHandler;
import net.blay09.mods.excompressum.registry.HeavySieveRegistry;

@Optional.Interface(modid = "MineTweaker3", iface = "minetweaker.util.IEventHandler", striprefs = true)
public class MineTweakerPostReload implements IEventHandler<MineTweakerImplementationAPI.ReloadEvent> {

    public MineTweakerPostReload() {
        MineTweakerImplementationAPI.onPostReload(this);
    }

    @Override
    public void handle(MineTweakerImplementationAPI.ReloadEvent reloadEvent) {
        HeavySieveRegistry.reload();
    }

}
