package net.blay09.mods.excompressum.forge;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.client.BalmClient;
import net.blay09.mods.balm.forge.platform.runtime.ForgeLoadContext;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.client.ExCompressumClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExCompressum.MOD_ID)
public class ForgeExCompressum {

    public ForgeExCompressum(FMLJavaModLoadingContext context) {
        final var loadContext = new ForgeLoadContext(context.getModBusGroup());
        Balm.initializeMod(ExCompressum.MOD_ID, loadContext, ExCompressum::initialize);
        DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> BalmClient.initializeMod(ExCompressum.MOD_ID, loadContext, ExCompressumClient::initialize));
    }
}
