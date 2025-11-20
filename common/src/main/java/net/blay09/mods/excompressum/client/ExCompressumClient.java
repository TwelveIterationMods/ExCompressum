package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.client.BalmClientRegistrars;

public class ExCompressumClient {
    public static void initialize(BalmClientRegistrars registrars) {
        registrars.menuScreens(ModScreens::initialize);
        registrars.entityRenderers(ModRenderers::initialize);
        registrars.blockStateModels(ModModels::initialize);
    }
}
