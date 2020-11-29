package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IComponentProvider;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.blay09.mods.excompressum.tile.WoodenCrucibleTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class WoodenCrucibleDataProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getTileEntity() instanceof WoodenCrucibleTileEntity) {
            WoodenCrucibleTileEntity tileEntity = (WoodenCrucibleTileEntity) accessor.getTileEntity();
            if (tileEntity.getSolidVolume() > 0f) {
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.solidVolume", (int) tileEntity.getSolidVolume()));
            }
            if (tileEntity.getFluidTank().getFluidAmount() > 0f) {
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.fluidVolume", (int) tileEntity.getFluidTank().getFluidAmount()));
            }
        }
    }

}
