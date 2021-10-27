package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.*;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.block.entity.HeavySieveBlockEntity;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public class HeavySieveDataProvider implements IBlockComponentProvider {

    @Override
    public void appendBody(ITooltip tooltip, IBlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof HeavySieveBlockEntity heavySieve) {
            if(heavySieve.getProgress() > 0f) {
                tooltip.add(new TranslatableComponent("excompressum.tooltip.sieveProgress", (int) (heavySieve.getProgress() * 100) + "%"));
            }
            ItemStack meshStack = heavySieve.getMeshStack();
            if (!meshStack.isEmpty()) {
                if(ExNihilo.getInstance().doMeshesHaveDurability()) {
                    tooltip.add(new TranslatableComponent("excompressum.tooltip.sieveMesh", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getDamageValue(), meshStack.getMaxDamage()));
                } else {
                    tooltip.add(meshStack.getDisplayName());
                }
            } else {
                tooltip.add(new TranslatableComponent("excompressum.tooltip.sieveNoMesh"));
            }
        }
    }

}
