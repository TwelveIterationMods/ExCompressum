package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.*;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.minecraft.network.chat.TranslatableComponent;

public class AutoSieveDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof AbstractAutoSieveBlockEntity autoSieve) {
            if(autoSieve.getCustomSkin() != null) {
                tooltip.add(new TranslatableComponent("excompressum.tooltip.sieveSkin", autoSieve.getCustomSkin().getName()));
            }
            if(autoSieve.getFoodBoost() > 1f) {
                tooltip.add(new TranslatableComponent("excompressum.tooltip.speedBoost", autoSieve.getFoodBoost()));
            }
            if(autoSieve.getEffectiveLuck() > 1) {
                tooltip.add(new TranslatableComponent("excompressum.tooltip.luckBonus", autoSieve.getEffectiveLuck() - 1));
            }

            tooltip.add(new TranslatableComponent("excompressum.tooltip.energyStoredOfMax", autoSieve.getEnergyStored(), autoSieve.getMaxEnergyStored()));
        }
    }

}
