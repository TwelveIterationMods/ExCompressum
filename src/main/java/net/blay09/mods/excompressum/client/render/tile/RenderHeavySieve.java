package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.tile.TileHeavySieve;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderHeavySieve extends TileEntitySpecialRenderer<TileHeavySieve> {

    @Override
    public void renderTileEntityAt(TileHeavySieve tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tileEntity.getCurrentStack() != null) {
            // TODO see CfB Milk Jar for content rendering

//            IIcon icon = tileEntity.getCurrentStack().getIconIndex();
//            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//
//            GlStateManager.pushMatrix();
//            GlStateManager.translate((float) x + 0.5f, (float) y + tileEntity.getAdjustedVolume(), (float) z + 0.5f);
//            contents.renderTop(icon);
//            GlStateManager.popMatrix();
//
//            GlStateManager.pushMatrix();
//            GlStateManager.translate((float) x + 0.5f, (float) y + 0.70f, (float) z + 0.5f);
//            contents.renderBottom(icon);
//            GlStateManager.popMatrix();
        }
    }

}
