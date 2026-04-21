package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoHeavySieveBlockEntity;
import net.blay09.mods.excompressum.block.entity.AutoSieveBlockEntity;
import net.blay09.mods.excompressum.client.ModModels;
import net.blay09.mods.excompressum.client.render.model.TinyHumanModel;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Quaternionf;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class AutoSieveRenderer<T extends AbstractAutoSieveBlockEntity> implements BlockEntityRenderer<T> {

    private static final RandomSource random = RandomSource.create();

    private final TinyHumanModel tinyHumanModel;
    private final TinyHumanModel tinyHumanModelSlim;
    private final boolean isHeavy;

    public static int cacheKey;
    private int currentCacheKey;
    private BakedModel sieveModel;

    public AutoSieveRenderer(BlockEntityRendererProvider.Context context, boolean isHeavy) {
        tinyHumanModel = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER), false);
        tinyHumanModelSlim = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER_SLIM), true);
        this.isHeavy = isHeavy;
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null || blockEntity.isUgly()) {
            return;
        }

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        if (sieveModel == null || currentCacheKey != cacheKey) {
            sieveModel = isHeavy ? dispatcher.getBlockModel(ModBlocks.heavySieves[0].defaultBlockState()) : ModModels.sieves.get(0).get();
            currentCacheKey = cacheKey;
        }

        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(blockEntity.getFacing().getRotation());
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(-90), 0f, 1f, 0f)));

        // Render the tiny human
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(-90), 0f, 0f, 1f)));
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(90), 0, 1f, 0)));
        poseStack.translate(0f, -1.2f, 0.25f);
        poseStack.scale(0.75f, 0.75f, 0.75f);
        final var skin = getPlayerSkin(blockEntity.getSkinProfile());
        TinyHumanModel playerModel = getPlayerModel(skin);
        playerModel.animate(blockEntity, partialTicks);
        playerModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entitySolid(skin.texture())), combinedLight, combinedOverlay, 0xFFFFFFFF);
        poseStack.popPose();

        // Render the glass around player head if underwater
        if (blockEntity.isWaterlogged()) {
            poseStack.pushPose();
            poseStack.translate(-0.95f, -0.42f, -0.175f);
            float glassScale = 0.35f;
            poseStack.scale(glassScale, glassScale, glassScale);
            dispatcher.renderSingleBlock(Blocks.GLASS.defaultBlockState(), poseStack, buffer, combinedLight, combinedOverlay);
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
        dispatcher.getModelRenderer().tesselateBlock(level, sieveModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.solid()), false, random, 0, Integer.MAX_VALUE);
        poseStack.popPose();

        // Render the sieve mesh
        SieveMeshRegistryEntry mesh = blockEntity.getSieveMesh();
        if (mesh != null) {
            BakedModel meshModel = ModModels.meshes.get(mesh.getModelName()).get();
            if (meshModel != null) {
                dispatcher.getModelRenderer().tesselateBlock(level, meshModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.translucent()), false, random, 0, Integer.MAX_VALUE);
            }
        }

        // Render the content
        ItemStack currentStack = blockEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (contentState != null) {
                float progress = blockEntity.getProgress();
                poseStack.pushPose();
                final var contentOffset = 0.0625f;
                final var meshY = 0.5625f;
                final var contentScaleXZ = 0.88f;
                final var contentBaseScaleY = 0.5f;
                poseStack.translate(contentOffset, meshY, contentOffset);
                poseStack.scale(contentScaleXZ, contentBaseScaleY - progress * contentBaseScaleY, contentScaleXZ);
                dispatcher.renderSingleBlock(contentState, poseStack, buffer, combinedLight, combinedOverlay);
                poseStack.popPose();
            }
        }
        poseStack.popPose();

        poseStack.popPose();
    }

    private PlayerSkin getPlayerSkin(@Nullable ResolvableProfile profile) {
        if (profile != null) {
            return Minecraft.getInstance().getSkinManager().getInsecureSkin(profile.gameProfile());
        } else {
            return DefaultPlayerSkin.get(UUID.randomUUID());
        }
    }

    private TinyHumanModel getPlayerModel(@Nullable PlayerSkin skin) {
        if (skin != null) {
            if ("slim".equals(skin.model().id())) {
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
