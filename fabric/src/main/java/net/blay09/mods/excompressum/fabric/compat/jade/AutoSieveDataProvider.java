package net.blay09.mods.excompressum.fabric.compat.jade;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class AutoSieveDataProvider implements IBlockComponentProvider {
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof AbstractAutoSieveBlockEntity autoSieve) {
            if(autoSieve.getSkinProfile() != null) {
                tooltip.add(Component.translatable("tooltip.excompressum.sieveSkin", autoSieve.getSkinProfile().partialProfile().name()));
            }
            if(autoSieve.getFoodBoost() > 1f) {
                tooltip.add(Component.translatable("tooltip.excompressum.speedBoost", autoSieve.getFoodBoost()));
            }
            if(autoSieve.getEffectiveLuck() > 1) {
                tooltip.add(Component.translatable("tooltip.excompressum.luckBonus", autoSieve.getEffectiveLuck() - 1));
            }

            tooltip.add(Component.translatable("tooltip.excompressum.energyStoredOfMax", autoSieve.getEnergyStored(), autoSieve.getMaxEnergyStored()));
        }
    }

    @Override
    public Identifier getUid() {
        return Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "auto_sieve");
    }
}
