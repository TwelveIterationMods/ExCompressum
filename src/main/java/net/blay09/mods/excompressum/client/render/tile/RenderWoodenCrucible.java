package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.tile.TileWoodenCrucible;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraftforge.fluids.Fluid;

public class RenderWoodenCrucible extends TileEntitySpecialRenderer<TileWoodenCrucible> {

    protected static BlockRendererDispatcher blockRenderer;

    public static IBakedModel modelSolid;
    public static IBakedModel modelFluid;

    @Override
    public void renderTileEntityAt(TileWoodenCrucible tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if(!tileEntity.hasWorldObj()) {
            return;
        }
        if(blockRenderer == null) {
            blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
        }

        if (tileEntity.hasSolids()) {
            GlStateManager.pushMatrix();
            float solidRenderVolume = Math.max(0.2f, Math.min(0.95f, (tileEntity.getSolidVolume() / TileWoodenCrucible.MAX_FLUID)));
            GlStateManager.translate((float) x + 0.5f, (float) y + solidRenderVolume, (float) z + 0.5f);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            // TODO color it maybe
//            IIcon icon = tileEntity.getCurrentMeltable().appearance.getIcon(0, tileEntity.getCurrentMeltable().appearanceMeta);
//            internal.render(new Color(tileEntity.getCurrentMeltable().appearance.getBlockColor()), icon, false);
            GlStateManager.popMatrix();
        }

        Fluid fluid = tileEntity.getFluid();
        if(fluid != null && tileEntity.getFluidAmount() > 0) {
            RenderHelper.disableStandardItemLighting();

            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.scale(1f, tileEntity.getFluidAmount() / tileEntity.getFluidCapacity(), 1f);
            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            int color = fluid.getColor();
            float alpha = (color >> 24 & 0xFF) / 255f;
            float red = (color >> 16 & 0xFF) / 255f;
            float green = (color >> 8 & 0xFF) / 255f;
            float blue = (color & 0xFF) / 255f;
            GlStateManager.color(red, green, blue, alpha);
            //Minecraft.getMinecraft().getRenderItem().renderModel(modelMilkLiquid, 0xFFFFFFFF);
            GlStateManager.popMatrix();

            RenderHelper.enableStandardItemLighting();
        }
    }




//    @Override
//    public void renderTileEntityAt(TileWoodenCrucible tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
//        // TODO check CfB Milk Jar to get the internal render stuff
//
//        // Render Solid Content
//        if (tileEntity.hasSolids()) {
//            GlStateManager.pushMatrix();
//            float solidRenderVolume = Math.max(0.2f, Math.min(0.95f, (tileEntity.getSolidVolume() / TileWoodenCrucible.MAX_FLUID)));
//            GlStateManager.translate((float) x + 0.5f, (float) y + solidRenderVolume, (float) z + 0.5f);
//            bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
////            IIcon icon = tileEntity.getCurrentMeltable().appearance.getIcon(0, tileEntity.getCurrentMeltable().appearanceMeta);
////            internal.render(new Color(tileEntity.getCurrentMeltable().appearance.getBlockColor()), icon, false);
//            GlStateManager.popMatrix();
//        }
//    }

}
