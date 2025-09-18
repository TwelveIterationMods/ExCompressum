package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.block.entity.HeavySieveBlockEntity;
import net.blay09.mods.excompressum.client.ModModels;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HeavySieveRenderer implements BlockEntityRenderer<HeavySieveBlockEntity, HeavySieveRenderer.HeavySieveRenderState> {

    public static class HeavySieveRenderState extends BlockEntityRenderState {
        public String meshModelName;
        public float progress;
        public ItemStackRenderState item;
    }

    private static final RandomSource random = RandomSource.create();

    public HeavySieveRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public HeavySieveRenderState createRenderState() {
        return new HeavySieveRenderState();
    }

    @Override
    public void extractRenderState(HeavySieveBlockEntity blockEntity, HeavySieveRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        renderState.meshModelName = blockEntity.getSieveMesh() != null ? blockEntity.getSieveMesh().getModelName() : null;
        renderState.progress = blockEntity.getProgress();

    }

    @Override
    public void submit(HeavySieveRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        final var meshModel = renderState.meshModelName != null ? ModModels.meshes.get(renderState.meshModelName).get() : null;
        if (meshModel != null) {
            submitNodeCollector.submitBlockModel(poseStack, RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS), meshModel, 0f, 0f, 0f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        }

        if (!renderState.item.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0625f, 0.5625f, 0.0625f);
            float tt = 0.42f;
            poseStack.scale(0.88f, tt - renderState.progress * tt, 0.88f);
            renderState.item.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

}
