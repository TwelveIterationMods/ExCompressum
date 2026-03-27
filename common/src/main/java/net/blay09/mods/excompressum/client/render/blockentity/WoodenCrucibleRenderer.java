package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.block.entity.WoodenCrucibleBlockEntity;
import net.blay09.mods.excompressum.client.ModModels;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.joml.Matrix4f;

public class WoodenCrucibleRenderer implements BlockEntityRenderer<WoodenCrucibleBlockEntity, WoodenCrucibleRenderer.WoodenCrucibleRenderState> {

    public static class WoodenCrucibleRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState content = new BlockModelRenderState();
        public final BlockModelRenderState fluid = new BlockModelRenderState();
        public final ItemStackRenderState item = new ItemStackRenderState();
        public int waterColor;
        public float fluidLevel;
        public float solidLevel;
    }

    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelResolver blockModelResolver;
    private final ItemModelResolver itemModelResolver;

    public WoodenCrucibleRenderer(BlockEntityRendererProvider.Context context) {
        blockModelResolver = context.blockModelResolver();
        itemModelResolver = context.itemModelResolver();
    }

    @Override
    public void extractRenderState(WoodenCrucibleBlockEntity blockEntity, WoodenCrucibleRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        blockModelResolver.update(renderState.content, Blocks.DARK_OAK_LEAVES.defaultBlockState(), BLOCK_DISPLAY_CONTEXT);

        final var fluidModel = ModModels.woodenCrucibleLiquid.asBlockStateModel();
        final var fluidParts = renderState.fluid.setupModel(new Matrix4f(), false);
        fluidModel.collectParts(renderState.fluid.scratchRandomSource(42), fluidParts);

        final var level = blockEntity.getLevel();
        final var waterColor = level != null ? level.getBiome(blockEntity.getBlockPos()).value().getWaterColor() : 0xFFFFFFFF;
        renderState.fluid.tintLayers().add(0, waterColor);

        itemModelResolver.updateForTopItem(renderState.item, blockEntity.getItem(0), ItemDisplayContext.FIXED, level, null, 0);
        renderState.fluidLevel = (float) blockEntity.getFluidTank().getAmount() / (float) blockEntity.getFluidTank().getCapacity();
        renderState.waterColor = level != null ? level.getBiome(blockEntity.getBlockPos()).value().getWaterColor() : 0xFFFFFFFF;
        renderState.solidLevel = (float) blockEntity.getSolidVolume() / (float) blockEntity.getSolidCapacity();
    }

    @Override
    public WoodenCrucibleRenderState createRenderState() {
        return new WoodenCrucibleRenderState();
    }

    @Override
    public void submit(WoodenCrucibleRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (!renderState.item.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0.0625f, 0.2f, 0.0625f);
            poseStack.scale(0.875f, 0.75f, 0.875f);
            renderState.item.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (renderState.fluidLevel > 0f) {
            poseStack.pushPose();
            poseStack.translate(0f, renderState.fluidLevel * 11 / 16f, 0f);
            renderState.fluid.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (renderState.solidLevel > 0) {
            poseStack.pushPose();
            poseStack.translate(0.0625f, 0.251f, 0.0625f);
            poseStack.scale(0.875f, (float) 0.71 * renderState.solidLevel, 0.875f);
            renderState.content.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
    }
}
