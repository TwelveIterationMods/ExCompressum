package net.blay09.mods.excompressum.compat.hudinfo;

import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoContext;
import net.blay09.mods.balm.platform.compatibility.hudinfo.BlockInfoProvider;
import net.blay09.mods.balm.platform.compatibility.hudinfo.HudInfoOutput;
import net.blay09.mods.excompressum.block.entity.HeavySieveBlockEntity;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class HeavySieveBlockInfoProvider implements BlockInfoProvider {
    @Override
    public void apply(BlockInfoContext context, HudInfoOutput output) {
        if (context.blockEntity() instanceof HeavySieveBlockEntity heavySieve) {
            if (heavySieve.getProgress() > 0f) {
                output.text(Component.translatable("tooltip.excompressum.sieveProgress", (int) (heavySieve.getProgress() * 100) + "%"));
            }
            ItemStack meshStack = heavySieve.getMeshStack();
            if (!meshStack.isEmpty()) {
                if (ExNihilo.getInstance().doMeshesHaveDurability()) {
                    output.text(Component.translatable("tooltip.excompressum.sieveMesh", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getDamageValue(), meshStack.getMaxDamage()));
                } else {
                    output.text(meshStack.getDisplayName());
                }
            } else {
                output.text(Component.translatable("tooltip.excompressum.sieveNoMesh"));
            }
        }
    }
}
