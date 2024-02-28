package net.blay09.mods.excompressum.mixin;

import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BinomialDistributionGenerator.class)
public interface BinomialDistributionGeneratorAccessor {
    @Accessor("n")
    NumberProvider getN();

    @Accessor("p")
    NumberProvider getP();
}
