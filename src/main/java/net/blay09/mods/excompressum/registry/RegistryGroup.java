package net.blay09.mods.excompressum.registry;

import net.minecraft.util.ResourceLocation;

public class RegistryGroup {
    private ResourceLocation id;
    private boolean enabledByDefault;

    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    public void setEnabledByDefault(boolean enabledByDefault) {
        this.enabledByDefault = enabledByDefault;
    }
}
