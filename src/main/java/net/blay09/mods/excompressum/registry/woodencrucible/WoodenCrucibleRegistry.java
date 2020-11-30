package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.registry.GroupedRegistry;
import net.blay09.mods.excompressum.registry.GroupedRegistryData;
import net.blay09.mods.excompressum.registry.RegistryGroup;
import net.blay09.mods.excompressum.registry.RegistryOverride;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

public class WoodenCrucibleRegistry extends GroupedRegistry<
        RegistryGroup,
        RegistryOverride,
        RegistryOverride,
        WoodenCrucibleMeltable,
        GroupedRegistryData<RegistryGroup, RegistryOverride, RegistryOverride, WoodenCrucibleMeltable>> {

    private final List<WoodenCrucibleMeltable> entries = new ArrayList<>();

    public WoodenCrucibleRegistry() {
        super("WoodenCrucible");
    }

    public Collection<WoodenCrucibleMeltable> getEntries() {
        return entries;
    }

    @Nullable
    public WoodenCrucibleMeltable getMeltable(ItemStack itemStack) {
        return entries.stream().filter(it -> it.getSource().test(itemStack)).findFirst().orElse(null);
    }

    @Override
    protected void reset() {
        super.reset();
        entries.clear();
    }

    @Override
    protected void loadEntry(WoodenCrucibleMeltable entry, @Nullable RegistryOverride groupOverride, @Nullable RegistryOverride entryOverride) {
        entries.add(entry);
    }

    @Override
    protected Class<? extends WoodenCrucibleRegistryData> getDataClass() {
        return WoodenCrucibleRegistryData.class;
    }

    @Override
    protected WoodenCrucibleRegistryData getEmptyData() {
        return new WoodenCrucibleRegistryData();
    }
}
