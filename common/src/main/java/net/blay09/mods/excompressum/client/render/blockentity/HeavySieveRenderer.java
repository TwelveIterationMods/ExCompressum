package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.block.entity.HeavySieveBlockEntity;
import net.blay09.mods.excompressum.client.ModModels;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class HeavySieveRenderer implements BlockEntityRenderer<HeavySieveBlockEntity, HeavySieveRenderer.HeavySieveRenderState> {

    public static class HeavySieveRenderState extends BlockEntityRenderState {
        @Nullable
        public String meshModelName;
        public float progress;
        public final ItemStackRenderState item = new ItemStackRenderState();
    }

    private final ItemModelResolver itemModelResolver;

    public HeavySieveRenderer(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
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

        itemModelResolver.updateForTopItem(renderState.item, blockEntity.getCurrentStack(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public void submit(HeavySieveRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        final var meshModel = renderState.meshModelName != null ? ModModels.meshes.get(renderState.meshModelName).asBlockStateModel() : null;
        if (meshModel != null) {
            submitNodeCollector.submitBlockModel(poseStack, RenderTypes.entitySolid(TextureAtlas.LOCATION_BLOCKS), meshModel, 1f, 1f, 1f, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
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
