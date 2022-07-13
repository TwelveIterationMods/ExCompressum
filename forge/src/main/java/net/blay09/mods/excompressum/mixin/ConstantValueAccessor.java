package net.blay09.mods.excompressum.mixin;

import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ConstantValue.class)
public interface ConstantValueAccessor {
    @Accessor
    float getValue();
}
