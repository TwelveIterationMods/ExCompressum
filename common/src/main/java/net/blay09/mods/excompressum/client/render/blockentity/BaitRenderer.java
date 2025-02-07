package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.block.BaitType;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Quaternionf;

public class BaitRenderer implements BlockEntityRenderer<BaitBlockEntity> {

    public BaitRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BaitBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int combinedLight, int combinedOverlay) {
        final var level = tileEntity.getLevel();
        if (level == null) {
            return;
        }

        BaitType baitType = tileEntity.getBaitType();
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        poseStack.pushPose();
        poseStack.translate(0.45, 0.05f, 0.45);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(90f), 1f, 0f, 0f)));
        if (!baitType.getDisplayItemFirst().isEmpty()) {
            itemRenderer.renderStatic(baitType.getDisplayItemFirst(),
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    buffers,
                    level,
                    0);
        }
        poseStack.translate(0.1f, 0f, -0.05f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(5f), 1f, 0f, 0f)));
        if (!baitType.getDisplayItemSecond().isEmpty()) {
            itemRenderer.renderStatic(baitType.getDisplayItemSecond(),
                    ItemDisplayContext.FIXED,
                    combinedLight,
                    combinedOverlay,
                    poseStack,
                    buffers,
                    level,
                    0);
        }
        poseStack.popPose();
    }
}
