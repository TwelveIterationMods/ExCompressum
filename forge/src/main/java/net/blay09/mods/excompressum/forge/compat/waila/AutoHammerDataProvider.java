package net.blay09.mods.excompressum.forge.compat.waila;

import mcp.mobius.waila.api.*;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.minecraft.network.chat.Component;

public class AutoHammerDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof AutoHammerBlockEntity autoHammer) {
            if(autoHammer.getEffectiveLuck() > 1) {
                tooltip.addLine(Component.translatable("tooltip.excompressum.luckBonus", autoHammer.getEffectiveLuck() - 1));
            }

            tooltip.addLine(Component.translatable("tooltip.excompressum.energyStoredOfMax", autoHammer.getEnergyStored(), autoHammer.getMaxEnergyStored()));
        }
    }

}
