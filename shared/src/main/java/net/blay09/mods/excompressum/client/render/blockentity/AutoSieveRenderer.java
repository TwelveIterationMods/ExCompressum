package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.registry.SieveModelBounds;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.block.HeavySieveBlock;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.client.ModModels;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.client.render.model.TinyHumanModel;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.Map;

public class AutoSieveRenderer implements BlockEntityRenderer<AbstractAutoSieveBlockEntity> {

    private static final RandomSource random = RandomSource.create();

    private final TinyHumanModel tinyHumanModel;
    private final TinyHumanModel tinyHumanModelSlim;
    private final boolean isHeavy;

    public static int cacheKey;
    private int currentCacheKey;
    private BakedModel sieveModel;

    public AutoSieveRenderer(BlockEntityRendererProvider.Context context, boolean isHeavy) {
        tinyHumanModel = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER), false);
        tinyHumanModelSlim = new TinyHumanModel(context.bakeLayer(ModelLayers.PLAYER), true);
        this.isHeavy = isHeavy;
    }

    @Override
    public void render(AbstractAutoSieveBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
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
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(RenderUtils.getRotationAngle(blockEntity.getFacing()), 0f, 1f, 0f)));

        // Render the tiny human
        poseStack.pushPose();
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(90, 0, 1f, 0)));
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(180, 1f, 0, 0)));
        poseStack.translate(0, -1.2f, 0.25f);
        poseStack.scale(0.75f, 0.75f, 0.75f);
        MinecraftProfileTexture skin = getPlayerSkin(blockEntity.getCustomSkin());
        TinyHumanModel playerModel = getPlayerModel(skin);
        playerModel.animate(blockEntity, partialTicks);
        playerModel.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entitySolid(getPlayerSkinTexture(skin))), combinedLight, combinedOverlay, 1f, 1f, 1f, 1f);
        poseStack.popPose();

        // Render the glass around player head if underwater
        if (blockEntity.isWaterlogged()) {
            poseStack.pushPose();
            poseStack.translate(-0.425f, 0.6f, -0.175f);
            float glassScale = 0.35f;
            poseStack.scale(glassScale, glassScale, glassScale);
            dispatcher.renderSingleBlock(Blocks.GLASS.defaultBlockState(), poseStack, buffer, combinedLight, combinedOverlay);
            poseStack.popPose();
        }

        // Sieve & Content
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.translate(-0.25f, 0f, -0.5f);

        // Render the sieve
        poseStack.pushPose();
        poseStack.translate(0f, 0.01f, 0f);
        dispatcher.getModelRenderer().tesselateBlock(level, sieveModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffer.getBuffer(RenderType.solid()), false, random, 0, Integer.MAX_VALUE);
        poseStack.popPose();

        SieveModelBounds bounds = HeavySieveBlock.SIEVE_BOUNDS;

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
                poseStack.translate(bounds.contentOffset, bounds.meshY, bounds.contentOffset);
                poseStack.scale(bounds.contentScaleXZ, bounds.contentBaseScaleY - progress * bounds.contentBaseScaleY, bounds.contentScaleXZ);
                dispatcher.renderSingleBlock(contentState, poseStack, buffer, combinedLight, combinedOverlay);
                poseStack.popPose();
            }
        }
        poseStack.popPose();

        poseStack.popPose();
    }

    @Nullable
    private MinecraftProfileTexture getPlayerSkin(@Nullable GameProfile customSkin) {
        if (customSkin != null) {
            final Minecraft mc = Minecraft.getInstance();
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = mc.getSkinManager().getInsecureSkinInformation(customSkin);
            return map.get(MinecraftProfileTexture.Type.SKIN);
        } else {
            return null;
        }
    }

    private TinyHumanModel getPlayerModel(@Nullable MinecraftProfileTexture customSkin) {
        if (customSkin != null) {
            String model = customSkin.getMetadata("model");
            if ("slim".equals(model)) {
                return tinyHumanModelSlim;
            }
        }

        return tinyHumanModel;
    }

    private ResourceLocation getPlayerSkinTexture(@Nullable MinecraftProfileTexture customSkin) {
        if (customSkin != null) {
            final Minecraft mc = Minecraft.getInstance();
            return mc.getSkinManager().registerTexture(customSkin, MinecraftProfileTexture.Type.SKIN);
        } else {
            return DefaultPlayerSkin.getDefaultSkin();
        }
    }

    public static <T extends AbstractAutoSieveBlockEntity> BlockEntityRenderer<T> normal(BlockEntityRendererProvider.Context context) {
        return (BlockEntityRenderer<T>) new AutoSieveRenderer(context, false);
    }

    public static <T extends AbstractAutoSieveBlockEntity> BlockEntityRenderer<T> heavy(BlockEntityRendererProvider.Context context) {
        return (BlockEntityRenderer<T>) new AutoSieveRenderer(context, true);
    }
}
