package net.blay09.mods.excompressum.registry;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class GroupedRegistryData<TGroup extends RegistryGroup, TGroupOverride extends RegistryOverride, TEntryOverride extends RegistryOverride, TEntry> {

    private String modId;
    private TGroup group;
    private Map<ResourceLocation, TGroupOverride> groupOverrides;
    private Map<ResourceLocation, TEntryOverride> entryOverrides;
    private List<TEntry> customEntries;

    @Nullable
    public String getModId() {
        return modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    @Nullable
    public TGroup getGroup() {
        return group;
    }

    public void setGroup(@Nullable TGroup group) {
        this.group = group;
    }

    @Nullable
    public Map<ResourceLocation, TGroupOverride> getGroupOverrides() {
        return groupOverrides;
    }

    public void setGroupOverrides(Map<ResourceLocation, TGroupOverride> groupOverrides) {
        this.groupOverrides = groupOverrides;
    }

    @Nullable
    public Map<ResourceLocation, TEntryOverride> getEntryOverrides() {
        return entryOverrides;
    }

    public void setEntryOverrides(Map<ResourceLocation, TEntryOverride> entryOverrides) {
        this.entryOverrides = entryOverrides;
    }

    @Nullable
    public List<TEntry> getCustomEntries() {
        return customEntries;
    }

    public void setCustomEntries(List<TEntry> customEntries) {
        this.customEntries = customEntries;
    }
}
