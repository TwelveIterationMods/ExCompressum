package net.blay09.mods.excompressum.api;

import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;

public interface ILootTableProvider {
    LootTable getLootTable(ResourceLocation resourceLocation, LootTableManager lootTableManager);
}
