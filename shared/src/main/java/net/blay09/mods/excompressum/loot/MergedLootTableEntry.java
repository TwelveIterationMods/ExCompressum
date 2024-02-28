package net.blay09.mods.excompressum.loot;

import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class MergedLootTableEntry {
    private final ItemStack itemStack;
    private final List<LootTableEntry> entries;

    public MergedLootTableEntry(LootTableEntry entry) {
        itemStack = entry.getItemStack();
        entries = Collections.singletonList(entry);
    }

    public MergedLootTableEntry(ItemStack itemStack, List<LootTableEntry> entries) {
        this.itemStack = itemStack;
        this.entries = entries;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<LootTableEntry> getEntries() {
        return entries;
    }
}
