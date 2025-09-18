package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoHeavySieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoSieveBlockEntity;
import net.blay09.mods.excompressum.client.ModModels;
import net.blay09.mods.excompressum.client.render.model.TinyHumanModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Quaternionf;

import java.util.UUID;

public class AutoSieveRenderer<T extends AbstractAutoSieveBlockEntity> implements BlockEntityRenderer<T, AutoSieveRenderer.AutoSieveRenderState> {

    public static class AutoSieveRenderState extends BlockEntityRenderState {
        public boolean skip;
        public Direction facing;
        public boolean waterlogged;
        public String meshModelName;
        public ItemStackRenderState item;
        public float progress;
    }

    private final BlockRenderDispatcher blockRenderDispatcher;
    private final ItemModelResolver itemModelResolver;
    private final TinyHumanModel tinyHumanModel;
    private final TinyHumanModel tinyHumanModelSlim;
    private final boolean isHeavy;

    public static int cacheKey;
    private int currentCacheKey;
    private BlockStateModel sieveModel;

    public AutoSieveRenderer(BlockEntityRendererProvider.Context context, boolean isHeavy) {
        blockRenderDispatcher = context.blockRenderDispatcher();
        itemModelResolver = context.itemModelResolver();
        tinyHumanModel = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER), false);
        tinyHumanModelSlim = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.isHeavy = isHeavy;
    }

    @Override
    public void extractRenderState(T blockEntity, AutoSieveRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        if (sieveModel == null || currentCacheKey != cacheKey) {
            sieveModel = isHeavy ? blockRenderDispatcher.getBlockModel(ModBlocks.heavySieves[0].defaultBlockState()) : ModModels.sieves.get(0).get();
            currentCacheKey = cacheKey;
        }

        itemModelResolver.updateForTopItem(renderState.item, blockEntity.getCurrentStack(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
    }

    @Override
    public AutoSieveRenderState createRenderState() {
        return new AutoSieveRenderState();
    }

    @Override
    public void submit(AutoSieveRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.skip) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(renderState.facing.getRotation());
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(-90), 0f, 1f, 0f)));

        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(-90), 0f, 0f, 1f)));
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(90), 0, 1f, 0)));
        poseStack.translate(0f, -0.65f, 0.25f);
        poseStack.scale(0.4f, 0.4f, 0.4f);
        // TODO final var skin = getPlayerSkin(blockEntity.getSkinProfile());
        // TODO TinyHumanModel playerModel = getPlayerModel(skin);
        // TODO playerModel.animate(blockEntity, partialTicks);
        // TODO playerModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(skin.texture())), combinedLight, combinedOverlay, 0xFFFFFFFF);
        poseStack.popPose();

        // Render the glass around player head if underwater
        if (renderState.waterlogged) {
            poseStack.pushPose();
            poseStack.translate(-0.95f, -0.42f, -0.175f);
            float glassScale = 0.35f;
            poseStack.scale(glassScale, glassScale, glassScale);
            // TODO dispatcher.renderSingleBlock(Blocks.GLASS.defaultBlockState(), poseStack, buffer, combinedLight, combinedOverlay);
            poseStack.popPose();
        }

        // Sieve & Content
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(-0.25f, 0f, -0.5f);

        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(90), 0f, 0f, 1f)));
        poseStack.translate(-0.2f, -0.1f, 0f);

        // Render the sieve
        poseStack.pushPose();
        // TODO dispatcher.getModelRenderer().tesselateBlock(level, sieveModel.collectParts(random), blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.solid()), false, Integer.MAX_VALUE);
        poseStack.popPose();

        // Render the sieve mesh
        final var meshModel = renderState.meshModelName != null ? ModModels.meshes.get(renderState.meshModelName).get() : null;
        if (meshModel != null) {
            // TODO dispatcher.getModelRenderer().tesselateBlock(level, meshModel.collectParts(random), blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.translucentMovingBlock()), false, Integer.MAX_VALUE);
        }

        // Render the content
        if (!renderState.item.isEmpty()) {
            poseStack.pushPose();
            final var contentOffset = 0.0625f;
            final var meshY = 0.5625f;
            final var contentScaleXZ = 0.88f;
            final var contentBaseScaleY = 0.5f;
            poseStack.translate(contentOffset, meshY, contentOffset);
            poseStack.scale(contentScaleXZ, contentBaseScaleY - renderState.progress * contentBaseScaleY, contentScaleXZ);
            // TODO dispatcher.renderSingleBlock(contentState, poseStack, buffer, combinedLight, combinedOverlay);
            poseStack.popPose();
        }
        poseStack.popPose();

        poseStack.popPose();
    }

    private PlayerSkin getPlayerSkin(@Nullable ResolvableProfile profile) {
        if (profile != null) {
            // TODO return Minecraft.getInstance().getSkinManager().getInsecureSkin(profile.gameProfile());
        }
        return DefaultPlayerSkin.get(UUID.randomUUID());
    }

    private TinyHumanModel getPlayerModel(@Nullable PlayerSkin skin) {
        if (skin != null) {
            if ("slim".equals(skin.model().getSerializedName())) {
                return tinyHumanModelSlim;
            }
        }

        return tinyHumanModel;
    }

    public static AutoSieveRenderer<AutoSieveBlockEntity> normal(BlockEntityRendererProvider.Context context) {
        return new AutoSieveRenderer<>(context, false);
    }

    public static AutoSieveRenderer<AutoHeavySieveBlockEntity> heavy(BlockEntityRendererProvider.Context context) {
        return new AutoSieveRenderer<>(context, true);
    }
}
