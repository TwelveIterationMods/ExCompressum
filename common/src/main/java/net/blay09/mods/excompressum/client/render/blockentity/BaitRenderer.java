package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.block.entity.BaitBlockEntity;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Quaternionf;

public class BaitRenderer implements BlockEntityRenderer<BaitBlockEntity, BaitRenderer.BaitRenderState> {

    public static class BaitRenderState extends BlockEntityRenderState {
        public final ItemStackRenderState firstItem = new ItemStackRenderState();
        public final ItemStackRenderState secondItem = new ItemStackRenderState();
    }

    private final ItemModelResolver itemModelResolver;

    public BaitRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public BaitRenderState createRenderState() {
        return new BaitRenderState();
    }

    @Override
    public void extractRenderState(BaitBlockEntity blockEntity, BaitRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderState.extractBase(blockEntity, renderState, crumblingOverlay);
        final var baitType = blockEntity.getBaitType();
        itemModelResolver.updateForTopItem(renderState.firstItem, baitType.getDisplayItemFirst().create(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.secondItem, baitType.getDisplayItemSecond().create(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(BaitRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.45, 0.05f, 0.45);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(90f), 1f, 0f, 0f)));
        renderState.firstItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.translate(0.1f, 0f, -0.05f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(5f), 1f, 0f, 0f)));
        renderState.secondItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();
    }

}
