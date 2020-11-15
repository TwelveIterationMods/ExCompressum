package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.registry.GroupedRegistryData;
import net.blay09.mods.excompressum.registry.RegistryGroup;
import net.blay09.mods.excompressum.registry.RegistryOverride;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class HeavySieveRegistryData extends GroupedRegistryData<RegistryGroup, RegistryOverride, RegistryOverride, HeavySiftable> {
    private Map<ResourceLocation, GeneratedHeavySiftable> generatedEntries;

    @Nullable
    public Map<ResourceLocation, GeneratedHeavySiftable> getGeneratedEntries() {
        return generatedEntries;
    }

    public void setGeneratedEntries(@Nullable Map<ResourceLocation, GeneratedHeavySiftable> generatedEntries) {
        this.generatedEntries = generatedEntries;
    }
}
