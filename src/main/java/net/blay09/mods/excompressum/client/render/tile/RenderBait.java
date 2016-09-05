package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.tile.TileBait;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderBait extends TileEntitySpecialRenderer<TileBait> {

    @Override
    public void renderTileEntityAt(TileBait tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        // TODO check CfB for item model rendering

        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.45f, (float) y + 0.05f, (float) z + 0.45f);

        GlStateManager.rotate(90f, 1f, 0f, 0f);
//        RenderItem.renderInFrame = true;
//        if(tileEntity.getRenderItem(0) != null) {
//            RenderManager.instance.renderEntityWithPosYaw(tileEntity.getRenderItem(0), 0, 0, 0, 0f, 0f);
//        }
        GlStateManager.rotate(-10f, 1f, 0f, 0f);
//        if(tileEntity.getRenderItem(1) != null) {
//            RenderManager.instance.renderEntityWithPosYaw(tileEntity.getRenderItem(1), 0, 0, 0, 0f, 0f);
//        }
//        RenderItem.renderInFrame = false;
        GlStateManager.popMatrix();
    }

}
