package net.blay09.mods.excompressum.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.excompressum.block.entity.WoodenCrucibleBlockEntity;
import net.minecraft.network.chat.Component;

public class WoodenCrucibleBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof WoodenCrucibleBlockEntity woodenCrucible) {
            if (woodenCrucible.getSolidVolume() > 0f) {
                output.text(Component.translatable("tooltip.excompressum.solidVolume", woodenCrucible.getSolidVolume()));
            }
            if (woodenCrucible.getFluidTank().getAmount() > 0f) {
                output.text(Component.translatable("tooltip.excompressum.fluidVolume", woodenCrucible.getFluidTank().getAmount()));
            }
        }
    }
}
