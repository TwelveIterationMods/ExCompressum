package net.blay09.mods.excompressum.loot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public record LootTableEntry(ItemStack itemStack, NumberProvider countRange, NumberProvider baseChance) {
}
