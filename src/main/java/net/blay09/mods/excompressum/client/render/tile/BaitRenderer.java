package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.tile.BaitTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.math.vector.Quaternion;

public class BaitRenderer extends TileEntityRenderer<BaitTileEntity> {

    public BaitRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(BaitTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        matrixStack.push();
        matrixStack.translate(0.45, 0.05, 0.45);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.rotate(new Quaternion(90f, 0f, 0f, true));
        /* TODO if (!tileEntity.getRenderItem(0).isEmpty()) {
            itemRenderer.renderItem(tileEntity.getRenderItem(0), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
        }*/
        matrixStack.rotate(new Quaternion(-10f, 0f, 0f, true));
        /* TODO if (!tileEntity.getRenderItem(1).isEmpty()) {
            itemRenderer.renderItem(tileEntity.getRenderItem(1), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
        }*/
        matrixStack.pop();
    }
}
