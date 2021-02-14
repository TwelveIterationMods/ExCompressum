package net.blay09.mods.excompressum.compat.patchouli;

import net.blay09.mods.excompressum.config.ExCompressumConfig;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliAddon {

    public PatchouliAddon() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigReloaded);
        updateConfigFlag();
    }

    private void onConfigReloaded(ModConfig.Reloading event) {
        updateConfigFlag();
    }

    private void updateConfigFlag() {
        PatchouliAPI.instance.setConfigFlag("excompressum:evolved_orechid_enabled", ExCompressumConfig.COMMON.enableEvolvedOrechid.get());
    }

}
