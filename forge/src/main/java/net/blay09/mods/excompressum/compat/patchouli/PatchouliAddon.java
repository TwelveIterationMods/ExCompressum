package net.blay09.mods.excompressum.compat.patchouli;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.ConfigReloadedEvent;
import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliAddon {

    public PatchouliAddon() {
        Balm.getEvents().onEvent(ConfigReloadedEvent.class, this::onConfigReloaded);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigReloaded);
        updateConfigFlag();
    }

    private void onConfigReloaded(ConfigReloadedEvent event) {
        updateConfigFlag();
    }

    private void updateConfigFlag() {
        PatchouliAPI.get().setConfigFlag("excompressum:evolved_orechid_enabled", ExCompressumConfig.getActive().compat.enableEvolvedOrechid);
    }

}
