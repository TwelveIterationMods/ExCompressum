package net.blay09.mods.excompressum.forge.compat.waila;

import mcp.mobius.waila.api.*;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.block.entity.HeavySieveBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class HeavySieveDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof HeavySieveBlockEntity heavySieve) {
            if(heavySieve.getProgress() > 0f) {
                tooltip.addLine(Component.translatable("excompressum.tooltip.sieveProgress", (int) (heavySieve.getProgress() * 100) + "%"));
            }
            ItemStack meshStack = heavySieve.getMeshStack();
            if (!meshStack.isEmpty()) {
                if(ExNihilo.getInstance().doMeshesHaveDurability()) {
                    tooltip.addLine(Component.translatable("excompressum.tooltip.sieveMesh", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getDamageValue(), meshStack.getMaxDamage()));
                } else {
                    tooltip.addLine(meshStack.getDisplayName());
                }
            } else {
                tooltip.addLine(Component.translatable("excompressum.tooltip.sieveNoMesh"));
            }
        }
    }

}
