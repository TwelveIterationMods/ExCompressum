package net.blay09.mods.excompressum.neoforge.compat.jade;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class AutoHammerDataProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof AutoHammerBlockEntity autoHammer) {
            if(autoHammer.getEffectiveLuck() > 1) {
                tooltip.add(Component.translatable("tooltip.excompressum.luckBonus", autoHammer.getEffectiveLuck() - 1));
            }

            tooltip.add(Component.translatable("tooltip.excompressum.energyStoredOfMax", autoHammer.getEnergyStored(), autoHammer.getMaxEnergyStored()));
        }
    }

    @Override
    public Identifier getUid() {
        return Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "auto_hammer");
    }
}
