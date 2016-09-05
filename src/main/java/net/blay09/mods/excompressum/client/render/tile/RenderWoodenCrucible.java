package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.tile.TileWoodenCrucible;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class RenderWoodenCrucible extends TileEntitySpecialRenderer<TileWoodenCrucible> {

    @Override
    public void renderTileEntityAt(TileWoodenCrucible tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        // TODO check CfB Milk Jar to get the internal render stuff

        // Render Solid Content
        if (tileEntity.hasSolids()) {
            GlStateManager.pushMatrix();
            float solidRenderVolume = Math.max(0.2f, Math.min(0.95f, (tileEntity.getSolidVolume() / TileWoodenCrucible.MAX_FLUID)));
            GlStateManager.translate((float) x + 0.5f, (float) y + solidRenderVolume, (float) z + 0.5f);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//            IIcon icon = tileEntity.getCurrentMeltable().appearance.getIcon(0, tileEntity.getCurrentMeltable().appearanceMeta);
//            internal.render(new Color(tileEntity.getCurrentMeltable().appearance.getBlockColor()), icon, false);
            GlStateManager.popMatrix();
        }

        // Render Fluid Content
        if(tileEntity.hasFluids()) {
            GlStateManager.pushMatrix();
            float fluidRenderVolume = Math.max(0.2f, Math.min(0.95f, (tileEntity.getFluidVolume() / TileWoodenCrucible.MAX_FLUID)));
            GlStateManager.translate((float) x + 0.5f, (float) y + fluidRenderVolume, (float) z + 0.5f);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
//            internal.render(new Color(tileEntity.getFluid().getColor()), tileEntity.getFluid().getIcon(), true);
            GlStateManager.popMatrix();
        }
    }

}
