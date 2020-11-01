package net.blay09.mods.excompressum.registry.chickenstick;

import net.blay09.mods.excompressum.registry.*;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

public class ChickenStickRegistry extends GroupedRegistry<
        RegistryGroup,
        RegistryOverride,
        RegistryOverride,
        ChickenStickHammerable,
        GroupedRegistryData<RegistryGroup, RegistryOverride, RegistryOverride, ChickenStickHammerable>> {

    private final Map<ResourceLocation, ChickenStickHammerable> entries = new HashMap<>();

    public ChickenStickRegistry() {
        super("ChickenStick");
    }

    public Collection<ChickenStickHammerable> getEntries() {
        return entries.values();
    }

    public boolean isHammerable(BlockState state) {
        final ResourceLocation registryName = state.getBlock().getRegistryName();
        return entries.containsKey(registryName);
    }

    @Override
    protected void reset() {
        super.reset();
        entries.clear();
    }

    @Override
    protected void loadEntry(ChickenStickHammerable entry, @Nullable RegistryOverride groupOverride, @Nullable RegistryOverride entryOverride) {
        entries.put(entry.getSource(), entry);
    }

    @Override
    protected Class<? extends ChickenStickRegistryData> getDataClass() {
        return ChickenStickRegistryData.class;
    }

    @Override
    protected ChickenStickRegistryData getEmptyData() {
        return new ChickenStickRegistryData();
    }

}
