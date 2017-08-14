package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.tile.TileBait;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderBait extends TileEntitySpecialRenderer<TileBait> {

    @Override
    public void render(TileBait tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x + 0.45, y + 0.05, z + 0.45);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.rotate(90f, 1f, 0f, 0f);
        if(!tileEntity.getRenderItem(0).isEmpty()) {
            itemRenderer.renderItem(tileEntity.getRenderItem(0), ItemCameraTransforms.TransformType.FIXED);
        }
        GlStateManager.rotate(-10f, 1f, 0f, 0f);
        if(!tileEntity.getRenderItem(1).isEmpty()) {
            itemRenderer.renderItem(tileEntity.getRenderItem(1), ItemCameraTransforms.TransformType.FIXED);
        }
        GlStateManager.popMatrix();
    }

}
