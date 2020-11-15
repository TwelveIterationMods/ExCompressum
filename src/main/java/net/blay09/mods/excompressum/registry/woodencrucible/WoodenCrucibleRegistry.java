package net.blay09.mods.excompressum.registry.woodencrucible;

import net.blay09.mods.excompressum.registry.GroupedRegistry;
import net.blay09.mods.excompressum.registry.GroupedRegistryData;
import net.blay09.mods.excompressum.registry.RegistryGroup;
import net.blay09.mods.excompressum.registry.RegistryOverride;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

public class WoodenCrucibleRegistry extends GroupedRegistry<
        RegistryGroup,
        RegistryOverride,
        RegistryOverride,
        WoodenCrucibleMeltable,
        GroupedRegistryData<RegistryGroup, RegistryOverride, RegistryOverride, WoodenCrucibleMeltable>> {

    private final Map<ResourceLocation, WoodenCrucibleMeltable> entries = new HashMap<>();
    private final Map<ResourceLocation, WoodenCrucibleMeltable> tagEntries = new HashMap<>();

    public WoodenCrucibleRegistry() {
        super("WoodenCrucible");
    }

    public Collection<WoodenCrucibleMeltable> getEntries() {
        return entries.values();
    }

    public Collection<WoodenCrucibleMeltable> getTagEntries() {
        return tagEntries.values();
    }

    @Nullable
    public WoodenCrucibleMeltable getMeltable(ItemStack itemStack) {
        WoodenCrucibleMeltable meltable = entries.get(itemStack.getItem().getRegistryName());
        if(meltable != null) {
            return meltable;
        }

        for (ResourceLocation tag : itemStack.getItem().getTags()) {
            meltable = tagEntries.get(tag);
            if(meltable != null) {
                return meltable;
            }
        }

        return null;
    }

    @Override
    protected void reset() {
        super.reset();
        entries.clear();
    }

    @Override
    protected void loadEntry(WoodenCrucibleMeltable entry, @Nullable RegistryOverride groupOverride, @Nullable RegistryOverride entryOverride) {
        if (entry.getSource().isTag()) {
            tagEntries.put(entry.getSource().getResourceLocation(), entry);
        } else {
            entries.put(entry.getSource().getResourceLocation(), entry);
        }
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
