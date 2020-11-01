package net.blay09.mods.excompressum.registry;

import net.minecraft.util.ResourceLocation;

public abstract class RegistryEntry {
    public abstract ResourceLocation getId();

    public boolean isEnabledByDefault() {
        return true;
    }
}
