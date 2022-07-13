package net.blay09.mods.excompressum.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;

public interface ILootTableProvider {
    LootTable getLootTable(ResourceLocation resourceLocation, LootTables lootTableManager);
}
