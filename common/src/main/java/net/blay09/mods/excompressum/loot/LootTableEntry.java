package net.blay09.mods.excompressum.loot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

public record LootTableEntry(ItemStack itemStack, ContextIntProvider countRange, ContextFloatProvider baseChance) {
}
