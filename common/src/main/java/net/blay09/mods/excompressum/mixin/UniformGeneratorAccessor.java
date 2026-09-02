package net.blay09.mods.excompressum.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.UniformGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(UniformGenerator.class)
public interface UniformGeneratorAccessor {
    @Accessor
    Holder<ContextIntProvider> getMin();
    @Accessor
    Holder<ContextIntProvider> getMax();
}
