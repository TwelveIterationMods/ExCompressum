package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.blay09.mods.excompressum.tile.BaitTileEntity;
import net.blay09.mods.excompressum.tile.EnvironmentalCondition;
import net.blay09.mods.excompressum.utils.Messages;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.core.StringLayout;

import java.util.List;

public class BaitDataProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getTileEntity() instanceof BaitTileEntity) {
            BaitTileEntity tileEntityBait = (BaitTileEntity) accessor.getTileEntity();
            EnvironmentalCondition environmentalStatus = tileEntityBait.checkSpawnConditions(true);
            if (environmentalStatus == EnvironmentalCondition.CanSpawn) {
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.baitTooClose"));
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.baitTooClose2"));
            } else {
                TranslationTextComponent statusText = new TranslationTextComponent(environmentalStatus.langKey);
                statusText.mergeStyle(TextFormatting.RED);
                tooltip.add(statusText);
            }
        }
    }

}
