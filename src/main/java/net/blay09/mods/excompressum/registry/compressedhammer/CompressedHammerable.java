package net.blay09.mods.excompressum.registry.compressedhammer;

import net.blay09.mods.excompressum.api.LootTableProvider;
import net.blay09.mods.excompressum.api.RegistryEntry;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class CompressedHammerable extends RegistryEntry {

    private ResourceLocation id;
    private Ingredient source;
    private LootTableProvider lootTable;

    public Ingredient getSource() {
        return source;
    }

    public void setSource(Ingredient source) {
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
        return lootTable != null ? lootTable.getLootTable(id.getPath(), context) : null;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
