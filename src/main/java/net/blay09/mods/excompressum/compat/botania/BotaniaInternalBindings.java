package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.particles.IParticleData;
import vazkii.botania.client.fx.WispParticleData;

public class BotaniaInternalBindings {
    public static IParticleData createWispParticle() {
        return WispParticleData.wisp((float) (0.1f + Math.random() * 0.125f), 0f, 1f, 0.125f);
    }
}
