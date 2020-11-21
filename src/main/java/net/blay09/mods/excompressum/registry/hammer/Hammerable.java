package net.blay09.mods.excompressum.registry.hammer;

import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.RegistryEntry;
import net.blay09.mods.excompressum.registry.TagOrResourceLocation;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class Hammerable extends RegistryEntry {

    private TagOrResourceLocation source;
    private LootTableProvider lootTable;

    public TagOrResourceLocation getSource() {
        return source;
    }

    public void setSource(TagOrResourceLocation source) {
        this.source = source;
    }

    @Nullable
    public LootTableProvider getLootTable() {
        return lootTable;
    }

    public void setLootTable(@Nullable LootTableProvider lootTable) {
        this.lootTable = lootTable;
    }

    @Nullable
    public LootTable getLootTable(LootContext context) {
        return lootTable != null ? lootTable.getLootTable(source.getResourceLocation().getPath(), context) : null;
    }

    @Override
    public ResourceLocation getId() {
        return getSource().getResourceLocation();
    }
}
