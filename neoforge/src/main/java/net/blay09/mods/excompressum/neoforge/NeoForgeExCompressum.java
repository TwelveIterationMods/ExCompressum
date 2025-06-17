package net.blay09.mods.excompressum.neoforge;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.neoforge.NeoForgeLoadContext;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.neoforge.compat.top.TheOneProbeAddon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(ExCompressum.MOD_ID)
public class NeoForgeExCompressum {

    private static final Logger logger = LoggerFactory.getLogger(NeoForgeExCompressum.class);

    public NeoForgeExCompressum(IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modEventBus);
        Balm.initializeMod(ExCompressum.MOD_ID, context, ExCompressum::initialize);

        modEventBus.addListener(this::imc);
    }

    private void imc(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded("theoneprobe")) {
            TheOneProbeAddon.register();
        }
    }

}
