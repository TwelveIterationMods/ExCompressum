package net.blay09.mods.excompressum.client;

import net.blay09.mods.excompressum.tile.TileEntityBait;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RenderBait extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        TileEntityBait tileEntityBait = (TileEntityBait) tileEntity;
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.45f, (float) y + 0.05f, (float) z + 0.45f);
        GL11.glRotatef(90f, 1f, 0f, 0f);
        RenderItem.renderInFrame = true;
        if(tileEntityBait.getRenderItem(0) != null) {
            RenderManager.instance.renderEntityWithPosYaw(tileEntityBait.getRenderItem(0), 0, 0, 0, 0f, 0f);
        }
        GL11.glRotatef(-10f, 1f, 0f, 0f);
        if(tileEntityBait.getRenderItem(1) != null) {
            RenderManager.instance.renderEntityWithPosYaw(tileEntityBait.getRenderItem(1), 0, 0, 0, 0f, 0f);
        }
        RenderItem.renderInFrame = false;
        GL11.glPopMatrix();
    }

}
