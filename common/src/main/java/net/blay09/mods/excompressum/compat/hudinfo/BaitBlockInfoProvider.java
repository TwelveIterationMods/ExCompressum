package net.blay09.mods.excompressum.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.blay09.mods.excompressum.block.entity.EnvironmentalConditionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class BaitBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof BaitBlockEntity bait) {
            EnvironmentalConditionResult environmentalStatus = bait.checkSpawnConditions(true);
            if (environmentalStatus == EnvironmentalConditionResult.CanSpawn) {
                output.text(Component.translatable("tooltip.excompressum.baitTooClose"));
                output.text(Component.translatable("tooltip.excompressum.baitTooClose2"));
            } else {
                output.text(Component.translatable(environmentalStatus.langKey, environmentalStatus.params).withStyle(ChatFormatting.RED));
            }
        }
    }
}
