package net.blay09.mods.excompressum.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Optional;

@Mixin(LootPoolEntryContainer.class)
public interface LootPoolSingletonContainerAccessor {
    @Accessor("modifier")
    Optional<Holder<LootItemFunction>> getModifier();
}
