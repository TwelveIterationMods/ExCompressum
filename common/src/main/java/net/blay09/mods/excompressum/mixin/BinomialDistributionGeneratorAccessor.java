package net.blay09.mods.excompressum.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BinomialDistributionGenerator.class)
public interface BinomialDistributionGeneratorAccessor {
    @Accessor("n")
    Holder<ContextIntProvider> getN();

    @Accessor("p")
    Holder<ContextFloatProvider> getP();
}
