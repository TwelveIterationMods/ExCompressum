package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.registry.LootTableProvider;
import net.blay09.mods.excompressum.registry.RegistryEntry;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class CompressedHammerable extends RegistryEntry {

    private ResourceLocation source;
    private LootTableProvider lootTable;

    public ResourceLocation getSource() {
        return source;
    }

    public void setSource(ResourceLocation source) {
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
        return lootTable != null ? lootTable.getLootTable(source.getPath(), context) : null;
    }

    @Override
    public ResourceLocation getId() {
        return getSource();
    }
}
