package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.client.BalmClient;

public class ExCompressumClient {
    public static void initialize() {
        ModScreens.register(BalmClient.getScreens());
        ModRenderers.initialize(BalmClient.getRenderers());
    }
}
