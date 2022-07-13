package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.blay09.mods.excompressum.block.BaitType;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;

public class BaitRenderer implements BlockEntityRenderer<BaitBlockEntity> {

    public BaitRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BaitBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int combinedLight, int combinedOverlay) {
        BaitType baitType = tileEntity.getBaitType();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        poseStack.translate(0.45, 0.05f, 0.45);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(new Quaternion(90f, 0f, 0f, true));
        if (!baitType.getDisplayItemFirst().isEmpty()) {
            itemRenderer.renderStatic(baitType.getDisplayItemFirst(), ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffers, 0);
        }
        poseStack.translate(0.1f, 0f, -0.05f);
        poseStack.mulPose(new Quaternion(5f, 0f, 0f, true));
        if (!baitType.getDisplayItemSecond().isEmpty()) {
            itemRenderer.renderStatic(baitType.getDisplayItemSecond(), ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffers, 0);
        }
        poseStack.popPose();
    }
}
