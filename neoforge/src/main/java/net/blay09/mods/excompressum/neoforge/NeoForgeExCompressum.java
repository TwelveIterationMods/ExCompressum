package net.blay09.mods.excompressum.neoforge;

import net.blay09.mods.balm.Balm;
import net.blay09.mods.balm.neoforge.platform.runtime.NeoForgeLoadContext;
import net.blay09.mods.excompressum.ExCompressum;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(ExCompressum.MOD_ID)
public class NeoForgeExCompressum {

    public NeoForgeExCompressum(ModContainer modContainer, IEventBus modEventBus) {
        final var context = new NeoForgeLoadContext(modContainer, modEventBus);
        Balm.initializeMod(ExCompressum.MOD_ID, context, ExCompressum::initialize);
    }

}
