package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.tile.TileEntityBait;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
