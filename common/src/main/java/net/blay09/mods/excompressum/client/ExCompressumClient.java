package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.event.client.ConnectedToServerEvent;
import net.blay09.mods.excompressum.ExCompressum;

public class ExCompressumClient {
    public static void initialize() {
        ModScreens.initialize(BalmClient.getScreens());
        ModRenderers.initialize(BalmClient.getRenderers());
        ModModels.initialize(BalmClient.getModels());

        Balm.getEvents().onEvent(ConnectedToServerEvent.class, event -> {
            ExCompressum.initializeAddons();
        });
    }
}
