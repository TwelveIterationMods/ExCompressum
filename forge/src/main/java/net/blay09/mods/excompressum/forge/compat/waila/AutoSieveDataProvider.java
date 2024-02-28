package net.blay09.mods.excompressum.forge.compat.waila;

import mcp.mobius.waila.api.*;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.network.chat.Component;

public class AutoSieveDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof AbstractAutoSieveBlockEntity autoSieve) {
            if(autoSieve.getCustomSkin() != null) {
                tooltip.addLine(Component.translatable("tooltip.excompressum.sieveSkin", autoSieve.getCustomSkin().getName()));
            }
            if(autoSieve.getFoodBoost() > 1f) {
                tooltip.addLine(Component.translatable("tooltip.excompressum.speedBoost", autoSieve.getFoodBoost()));
            }
            if(autoSieve.getEffectiveLuck() > 1) {
                tooltip.addLine(Component.translatable("tooltip.excompressum.luckBonus", autoSieve.getEffectiveLuck() - 1));
            }

            tooltip.addLine(Component.translatable("tooltip.excompressum.energyStoredOfMax", autoSieve.getEnergyStored(), autoSieve.getMaxEnergyStored()));
        }
    }

}
