package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.*;

import net.blay09.mods.excompressum.block.entity.WoodenCrucibleBlockEntity;
import net.minecraft.network.chat.TranslatableComponent;

public class WoodenCrucibleDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof WoodenCrucibleBlockEntity woodenCrucible) {
            if (woodenCrucible.getSolidVolume() > 0f) {
                tooltip.add(new TranslatableComponent("excompressum.tooltip.solidVolume", woodenCrucible.getSolidVolume()));
            }
            if (woodenCrucible.getFluidTank().getFluidAmount() > 0f) {
                tooltip.add(new TranslatableComponent("excompressum.tooltip.fluidVolume", woodenCrucible.getFluidTank().getFluidAmount()));
            }
        }
    }

}
