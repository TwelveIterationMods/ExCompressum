package net.blay09.mods.excompressum.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataResolver;
import net.minecraft.world.level.storage.loot.LootTable;

public interface ILootTableProvider {
    LootTable getLootTable(ResourceLocation resourceLocation, LootDataResolver lootDataResolver);
}
