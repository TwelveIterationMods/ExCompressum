package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.block.BaitType;
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
        BaitType baitType = tileEntity.getBaitType();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        matrixStack.push();
        matrixStack.translate(0.45, 0.05f, 0.45);
        matrixStack.scale(0.5f, 0.5f, 0.5f);
        matrixStack.rotate(new Quaternion(90f, 0f, 0f, true));
        if (!baitType.getDisplayItemFirst().isEmpty()) {
            itemRenderer.renderItem(baitType.getDisplayItemFirst(), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
        }
        matrixStack.translate(0.1f, 0f, -0.05f);
        matrixStack.rotate(new Quaternion(5f, 0f, 0f, true));
        if (!baitType.getDisplayItemSecond().isEmpty()) {
            itemRenderer.renderItem(baitType.getDisplayItemSecond(), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, combinedOverlayIn, matrixStack, bufferIn);
        }
        matrixStack.pop();
    }
}
