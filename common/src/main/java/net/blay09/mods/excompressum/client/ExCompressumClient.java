package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmClientRegistrars;
import net.blay09.mods.balm.client.platform.event.callback.ClientLifecycleCallback;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.compat.recipeviewers.ExCompressumRecipeViewerProvider;

import static net.blay09.mods.excompressum.ExCompressum.id;

public class ExCompressumClient {
    public static void initialize(BalmClientRegistrars registrars) {
        registrars.menuScreens(ModScreens::initialize);
        registrars.blockEntityRenderers(ModRenderers::initialize);
        registrars.entityRenderers(ModRenderers::initialize);
        registrars.blockStateModels(ModModels::initialize);
        Balm.modSupport().recipeViewers().register(id("recipes"), new ExCompressumRecipeViewerProvider());

        ClientLifecycleCallback.ConnectedToServer.EVENT.register(client -> {
            ExCompressum.initializeAddons();
        });
    }
}
