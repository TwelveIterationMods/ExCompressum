package net.blay09.mods.excompressum.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.minecraft.network.chat.Component;

public class AutoHammerBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof AutoHammerBlockEntity autoHammer) {
            if (autoHammer.getEffectiveLuck() > 1) {
                output.text(Component.translatable("tooltip.excompressum.luckBonus", autoHammer.getEffectiveLuck() - 1));
            }
            output.text(Component.translatable("tooltip.excompressum.energyStoredOfMax", autoHammer.getEnergyStored(), autoHammer.getMaxEnergyStored()));
        }
    }
}
