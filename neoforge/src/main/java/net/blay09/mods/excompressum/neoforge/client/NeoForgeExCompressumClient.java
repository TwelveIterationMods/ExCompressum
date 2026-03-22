package net.blay09.mods.excompressum.neoforge.client;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.client.ExCompressumClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = ExCompressum.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeExCompressumClient {

    public NeoForgeExCompressumClient(ModContainer modContainer, IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modContainer, modEventBus);
        BalmClient.initializeMod(ExCompressum.MOD_ID, context, ExCompressumClient::initialize);
    }
}
