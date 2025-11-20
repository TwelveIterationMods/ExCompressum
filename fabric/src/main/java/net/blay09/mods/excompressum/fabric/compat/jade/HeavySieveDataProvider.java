package net.blay09.mods.excompressum.fabric.compat.jade;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.entity.HeavySieveBlockEntity;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public class HeavySieveDataProvider implements IBlockComponentProvider {

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if(accessor.getBlockEntity() instanceof HeavySieveBlockEntity heavySieve) {
            if(heavySieve.getProgress() > 0f) {
                tooltip.add(Component.translatable("tooltip.excompressum.sieveProgress", (int) (heavySieve.getProgress() * 100) + "%"));
            }
            ItemStack meshStack = heavySieve.getMeshStack();
            if (!meshStack.isEmpty()) {
                if(ExNihilo.getInstance().doMeshesHaveDurability()) {
                    tooltip.add(Component.translatable("tooltip.excompressum.sieveMesh", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getDamageValue(), meshStack.getMaxDamage()));
                } else {
                    tooltip.add(meshStack.getDisplayName());
                }
            } else {
                tooltip.add(Component.translatable("tooltip.excompressum.sieveNoMesh"));
            }
        }
    }

    @Override
    public Identifier getUid() {
        return Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, "heavy_sieve");
    }

}
