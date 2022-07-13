package net.blay09.mods.excompressum.compat.botania;

import net.minecraft.core.particles.ParticleOptions;
import vazkii.botania.client.fx.WispParticleData;

class BotaniaInternalBindingsImpl implements BotaniaInternalBindings {
    @Override
    public ParticleOptions createWispParticle() {
        return WispParticleData.wisp((float) (0.1f + Math.random() * 0.125f), 0f, 1f, 0.125f);
    }
}
