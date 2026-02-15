package net.blay09.mods.excompressum.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.network.chat.Component;

public class AutoSieveBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof AbstractAutoSieveBlockEntity autoSieve) {
            if (autoSieve.getSkinProfile() != null) {
                output.text(Component.translatable("tooltip.excompressum.sieveSkin", autoSieve.getSkinProfile().partialProfile().name()));
            }
            if (autoSieve.getFoodBoost() > 1f) {
                output.text(Component.translatable("tooltip.excompressum.speedBoost", autoSieve.getFoodBoost()));
            }
            if (autoSieve.getEffectiveLuck() > 1) {
                output.text(Component.translatable("tooltip.excompressum.luckBonus", autoSieve.getEffectiveLuck() - 1));
            }
            output.text(Component.translatable("tooltip.excompressum.energyStoredOfMax", autoSieve.getEnergyStored(), autoSieve.getMaxEnergyStored()));
        }
    }
}
