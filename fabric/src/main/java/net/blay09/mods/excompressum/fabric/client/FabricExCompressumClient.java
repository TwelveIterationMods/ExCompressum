package net.blay09.mods.excompressum.fabric.client;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.fabric.platform.runtime.FabricLoadContext;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.client.ExCompressumClient;
import net.fabricmc.api.ClientModInitializer;

public class FabricExCompressumClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BalmClient.initializeMod(ExCompressum.MOD_ID, FabricLoadContext.INSTANCE, ExCompressumClient::initialize);
    }
}
