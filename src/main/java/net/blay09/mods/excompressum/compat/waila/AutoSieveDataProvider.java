package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class AutoSieveDataProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(accessor.getTileEntity() instanceof AutoSieveTileEntityBase) {
            AutoSieveTileEntityBase tileEntity = (AutoSieveTileEntityBase) accessor.getTileEntity();
            if(tileEntity.getCustomSkin() != null) {
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.sieveSkin", tileEntity.getCustomSkin().getName()));
            }
            if(tileEntity.getFoodBoost() > 1f) {
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.speedBoost", tileEntity.getFoodBoost()));
            }
            if(tileEntity.getEffectiveLuck() > 1) {
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.luckBonus", tileEntity.getEffectiveLuck() - 1));
            }

            tooltip.add(new TranslationTextComponent("excompressum.tooltip.energyStoredOfMax", tileEntity.getEnergyStored(), tileEntity.getMaxEnergyStored()));
        }
    }

}
