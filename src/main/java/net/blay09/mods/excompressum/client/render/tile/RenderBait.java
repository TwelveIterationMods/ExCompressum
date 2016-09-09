package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.tile.TileBait;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;

public class RenderBait extends TileEntitySpecialRenderer<TileBait> {

    @Override
    public void renderTileEntityAt(TileBait tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate(x + 0.45, y + 0.05, z + 0.45);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.rotate(90f, 1f, 0f, 0f);
        if(tileEntity.getRenderItem(0) != null) {
            renderItem(itemRenderer, tileEntity.getRenderItem(0), 0f, 0f, 0f, 0f, 0f, 0f, 0f);
        }
        GlStateManager.rotate(-10f, 1f, 0f, 0f);
        if(tileEntity.getRenderItem(1) != null) {
            renderItem(itemRenderer, tileEntity.getRenderItem(1), 0.01f, 0f, 0f, 0f, 0f, 0f, 0f);
        }
        GlStateManager.popMatrix();
    }

    private static void renderItem(RenderItem itemRenderer, ItemStack itemStack, float x, float y, float z, float angle, float xr, float yr, float zr) {
        GlStateManager.pushMatrix(); // TODO optimize this
        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(angle, xr, yr, zr);
        if (!itemRenderer.shouldRenderItemIn3D(itemStack)) {
            GlStateManager.rotate(180f, 0f, 1f, 0f);
        }
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        itemRenderer.renderItem(itemStack, ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
    }

}
