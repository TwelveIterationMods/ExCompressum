package net.blay09.mods.excompressum.registry.heavysieve;

import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

public class HeavySiftableKey {
    private final ResourceLocation source;
    private final SieveMeshRegistryEntry mesh;
    private final boolean waterlogged;

    public HeavySiftableKey(ResourceLocation source, @Nullable SieveMeshRegistryEntry mesh, boolean waterlogged) {
        this.source = source;
        this.mesh = mesh;
        this.waterlogged = waterlogged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HeavySiftableKey that = (HeavySiftableKey) o;
        return waterlogged == that.waterlogged &&
                source.equals(that.source) &&
                Objects.equals(mesh, that.mesh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source, mesh, waterlogged);
    }
}
