package net.blay09.mods.excompressum.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Optional;

@Mixin(LootPool.class)
public interface LootPoolAccessor {
    @Accessor
    Optional<Holder<LootItemCondition>> getCondition();
    @Accessor
    List<LootPoolEntryContainer> getEntries();
}
