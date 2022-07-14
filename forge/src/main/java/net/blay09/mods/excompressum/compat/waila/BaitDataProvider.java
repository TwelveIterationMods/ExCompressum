package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.*;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.blay09.mods.excompressum.block.entity.EnvironmentalConditionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;

public class BaitDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof BaitBlockEntity bait) {
            EnvironmentalConditionResult environmentalStatus = bait.checkSpawnConditions(true);
            if (environmentalStatus == EnvironmentalConditionResult.CanSpawn) {
                tooltip.add(new TranslatableComponent("excompressum.tooltip.baitTooClose"));
                tooltip.add(new TranslatableComponent("excompressum.tooltip.baitTooClose2"));
            } else {
                TranslatableComponent statusText = new TranslatableComponent(environmentalStatus.langKey, environmentalStatus.params);
                statusText.withStyle(ChatFormatting.RED);
                tooltip.add(statusText);
            }
        }
    }

}
