package net.blay09.mods.excompressum.neoforge.compat.jade;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.blay09.mods.excompressum.block.entity.EnvironmentalConditionResult;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class BaitDataProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockEntity() instanceof BaitBlockEntity bait) {
            EnvironmentalConditionResult environmentalStatus = bait.checkSpawnConditions(true);
            if (environmentalStatus == EnvironmentalConditionResult.CanSpawn) {
                tooltip.add(Component.translatable("tooltip.excompressum.baitTooClose"));
                tooltip.add(Component.translatable("tooltip.excompressum.baitTooClose2"));
            } else {
                final var statusText = Component.translatable(environmentalStatus.langKey, environmentalStatus.params);
                statusText.withStyle(ChatFormatting.RED);
                tooltip.add(statusText);
            }
        }
    }

    @Override
    public Identifier getUid() {
        return Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "bait");
    }

}
