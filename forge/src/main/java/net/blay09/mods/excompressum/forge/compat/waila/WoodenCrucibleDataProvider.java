package net.blay09.mods.excompressum.forge.compat.waila;

import mcp.mobius.waila.api.*;

import net.blay09.mods.excompressum.block.entity.WoodenCrucibleBlockEntity;
import net.minecraft.network.chat.Component;

public class WoodenCrucibleDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof WoodenCrucibleBlockEntity woodenCrucible) {
            if (woodenCrucible.getSolidVolume() > 0f) {
                tooltip.addLine(Component.translatable("tooltip.excompressum.solidVolume", woodenCrucible.getSolidVolume()));
            }
            if (woodenCrucible.getFluidTank().getAmount() > 0f) {
                tooltip.addLine(Component.translatable("tooltip.excompressum.fluidVolume", woodenCrucible.getFluidTank().getAmount()));
            }
        }
    }

}
