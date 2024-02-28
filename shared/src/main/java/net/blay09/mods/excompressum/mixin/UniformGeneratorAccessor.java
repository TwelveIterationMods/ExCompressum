package net.blay09.mods.excompressum.mixin;

import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(UniformGenerator.class)
public interface UniformGeneratorAccessor {
    @Accessor
    NumberProvider getMin();
    @Accessor
    NumberProvider getMax();
}
