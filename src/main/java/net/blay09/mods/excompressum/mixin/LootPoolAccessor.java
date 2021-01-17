package net.blay09.mods.excompressum.mixin;

import net.minecraft.loot.LootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.conditions.ILootCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LootPool.class)
public interface LootPoolAccessor {
    @Accessor
    List<ILootCondition> getConditions();
    @Accessor
    List<LootEntry> getLootEntries();
}
