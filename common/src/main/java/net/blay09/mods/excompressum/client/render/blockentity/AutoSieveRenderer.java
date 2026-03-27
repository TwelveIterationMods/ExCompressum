package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.block.AutoSieveBlock;
import net.blay09.mods.excompressum.block.HeavySieveType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoHeavySieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.SieveAnimationType;
import net.blay09.mods.excompressum.client.ModModels;
import net.blay09.mods.excompressum.client.render.model.TinyHumanModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.UUID;

public class AutoSieveRenderer<T extends AbstractAutoSieveBlockEntity> implements BlockEntityRenderer<T, AutoSieveRenderer.AutoSieveRenderState> {

    public static class AutoSieveRenderState extends BlockEntityRenderState {
        public final BlockModelRenderState glass = new BlockModelRenderState();
        public final BlockModelRenderState mesh = new BlockModelRenderState();
        public final BlockModelRenderState sieve = new BlockModelRenderState();
        public boolean skip;
        public Direction facing = Direction.NORTH;
        public boolean waterlogged;
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float progress;
        @Nullable
        public ResolvableProfile profile;
        public SieveAnimationType animationType = SieveAnimationType.DEFAULT;
        public float armAngle;
        public final AvatarRenderState avatar = new AvatarRenderState();
    }

    private static final BlockDisplayContext GLASS_BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private static final BlockDisplayContext SIEVE_BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelResolver blockModelResolver;
    private final ItemModelResolver itemModelResolver;
    private final TinyHumanModel tinyHumanModel;
    private final TinyHumanModel tinyHumanModelSlim;
    private final boolean isHeavy;

    public AutoSieveRenderer(BlockEntityRendererProvider.Context context, boolean isHeavy) {
        blockModelResolver = context.blockModelResolver();
        itemModelResolver = context.itemModelResolver();
        tinyHumanModel = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER), false);
        tinyHumanModelSlim = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.isHeavy = isHeavy;
    }

    @Override
    public void extractRenderState(T blockEntity, AutoSieveRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        blockModelResolver.update(renderState.glass, Blocks.GLASS.defaultBlockState(), GLASS_BLOCK_DISPLAY_CONTEXT);

        if (isHeavy) {
            final var sieveState = ModBlocks.heavySieves.get(HeavySieveType.OAK).defaultBlockState();
            blockModelResolver.update(renderState.sieve, sieveState, SIEVE_BLOCK_DISPLAY_CONTEXT);
        } else {
            final var sieveModel = ModModels.sieves.get(HeavySieveType.OAK).asBlockStateModel();
            final var sieveParts = renderState.sieve.setupModel(new Matrix4f(), false);
            sieveModel.collectParts(renderState.sieve.scratchRandomSource(42), sieveParts);
        }

        if (blockEntity.shouldAnimate()) {
            final var animationSpeed = blockEntity.getAnimationType() == SieveAnimationType.MAGIC ? 0.05f : 0.5f;
            blockEntity.armAngle += animationSpeed * delta;
            blockEntity.armAngle += animationSpeed * (Math.max(1f, blockEntity.getSpeedMultiplier() / 4f)) * delta;
        }

        renderState.skip = blockEntity.isUgly();
        renderState.progress = blockEntity.getProgress();

        final var meshModelName = blockEntity.getSieveMesh() != null ? blockEntity.getSieveMesh().getModelName() : null;
        final var meshModel = meshModelName != null ? ModModels.meshes.get(meshModelName).asBlockStateModel() : null;
        if (meshModel != null) {
            final var meshParts = renderState.mesh.setupModel(new Matrix4f(), false);
            meshModel.collectParts(renderState.mesh.scratchRandomSource(42), meshParts);
        } else {
            renderState.mesh.clear();
        }

        final var blockState = blockEntity.getBlockState();
        renderState.facing = blockState.getValue(AutoSieveBlock.FACING);
        renderState.waterlogged = blockState.getValue(AutoSieveBlock.WATERLOGGED);
        renderState.profile = blockEntity.getSkinProfile();
        renderState.armAngle = blockEntity.armAngle;

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
        final var skin = getPlayerSkin(renderState.profile);
        final var playerModel = getPlayerModel(skin);
        playerModel.animate(renderState);
        submitNodeCollector.submitModel(playerModel, renderState.avatar, poseStack, RenderTypes.entityCutout(skin.body().texturePath()), renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0, renderState.breakProgress);
        poseStack.popPose();

        // Render the glass around player head if underwater
        if (renderState.waterlogged) {
            poseStack.pushPose();
            poseStack.translate(-0.95f, -0.42f, -0.175f);
            float glassScale = 0.35f;
            poseStack.scale(glassScale, glassScale, glassScale);
            renderState.glass.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
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
        renderState.sieve.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();

        // Render the sieve mesh
        renderState.mesh.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        // Render the content
        if (!renderState.item.isEmpty()) {
            poseStack.pushPose();
            final var contentOffset = 0.0625f;
            final var meshY = 0.5625f;
            final var contentScaleXZ = 0.88f;
            final var contentBaseScaleY = 0.5f;
            poseStack.translate(contentOffset, meshY, contentOffset);
            poseStack.scale(contentScaleXZ, contentBaseScaleY - renderState.progress * contentBaseScaleY, contentScaleXZ);
            renderState.item.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }
        poseStack.popPose();

        poseStack.popPose();
    }

    private PlayerSkin getPlayerSkin(@Nullable ResolvableProfile profile) {
        if (profile != null) {
            return Minecraft.getInstance().playerSkinRenderCache().getOrDefault(profile).playerSkin();
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
