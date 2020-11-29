package net.blay09.mods.excompressum.compat.waila;

import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.tile.HeavySieveTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class HeavySieveDataProvider implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if(accessor.getTileEntity() instanceof HeavySieveTileEntity) {
            HeavySieveTileEntity tileEntity = (HeavySieveTileEntity) accessor.getTileEntity();
            if(tileEntity.getProgress() > 0f) {
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.sieveProgress", (int) (tileEntity.getProgress() * 100) + "%"));
            }
            ItemStack meshStack = tileEntity.getMeshStack();
            if (!meshStack.isEmpty()) {
                if(ExNihilo.getInstance().doMeshesHaveDurability()) {
                    tooltip.add(new TranslationTextComponent("excompressum.tooltip.sieveMesh", meshStack.getDisplayName(), meshStack.getMaxDamage() - meshStack.getDamage(), meshStack.getMaxDamage()));
                } else {
                    tooltip.add(meshStack.getDisplayName());
                }
            } else {
                tooltip.add(new TranslationTextComponent("excompressum.tooltip.sieveNoMesh"));
            }
        }
    }

}
