package net.blay09.mods.excompressum.config;

import net.blay09.mods.balm.api.Balm;

public class ExCompressumConfig {

    public static void initialize() {
        Balm.getConfig().registerConfig(ExCompressumConfigData.class, null);
    }

    public static ExCompressumConfigData getActive() {
        return Balm.getConfig().getActive(ExCompressumConfigData.class);
    }
}
