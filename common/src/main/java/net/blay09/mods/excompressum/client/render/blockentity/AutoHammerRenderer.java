package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.blay09.mods.excompressum.block.AutoHammerBlock;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;
import org.joml.Math;

import java.util.ArrayList;
import java.util.List;

public class AutoHammerRenderer implements BlockEntityRenderer<AutoHammerBlockEntity, AutoHammerRenderer.AutoHammerRenderState> {

    public static class AutoHammerRenderState extends BlockEntityRenderState {
        public boolean skip;
        public Direction facing = Direction.NORTH;
        public float hammerAngle;
        public final ItemStackRenderState hammerItem = new ItemStackRenderState();
        public final ItemStackRenderState firstHammerItem = new ItemStackRenderState();
        public final ItemStackRenderState secondHammerItem = new ItemStackRenderState();
        public final BlockModelRenderState content = new BlockModelRenderState();
        public final List<BlockStateModelPart> breakingContentParts = new ArrayList<>();
        public BlockState contentState;
        public float progress;
    }

    private static final BlockDisplayContext CONTENT_BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final BlockModelResolver blockModelResolver;
    private final ItemModelResolver itemModelResolver;
    private final boolean isCompressed;

    private ItemStack hammerItemStack = ItemStack.EMPTY;

    public AutoHammerRenderer(BlockEntityRendererProvider.Context context, boolean isCompressed) {
        this.blockModelResolver = context.blockModelResolver();
        this.itemModelResolver = context.itemModelResolver();
        this.isCompressed = isCompressed;
    }

    @Override
    public void extractRenderState(AutoHammerBlockEntity blockEntity, AutoHammerRenderState renderState, float delta, Vec3 vec, ModelFeatureRenderer.@Nullable CrumblingOverlay crumblingOverlay) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, delta, vec, crumblingOverlay);

        if (blockEntity.shouldAnimate()) {
            blockEntity.hammerAngle += 0.4f * delta;
        }

        renderState.skip = blockEntity.isUgly();
        renderState.facing = blockEntity.getBlockState().getValue(AutoHammerBlock.FACING);
        renderState.progress = blockEntity.getProgress();
        renderState.hammerAngle = blockEntity.hammerAngle;

        if (hammerItemStack.isEmpty()) {
            if (isCompressed) {
                hammerItemStack = ModItems.compressedDiamondHammer.createStack();
            } else {
                for (final var itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(ModItemTags.DIAMOND_HAMMERS)) {
                    hammerItemStack = new ItemStack(itemHolder.value());
                    break;
                }
                if (hammerItemStack.isEmpty()) {
                    hammerItemStack = new ItemStack(Items.DIAMOND_PICKAXE);
                }
            }
        }

        itemModelResolver.updateForTopItem(renderState.hammerItem, hammerItemStack, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.firstHammerItem, blockEntity.getUpgradeStack(0), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
        itemModelResolver.updateForTopItem(renderState.secondHammerItem, blockEntity.getUpgradeStack(1), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);

        final var contentState = StupidUtils.getStateFromItemStack(blockEntity.getCurrentStack());
        renderState.contentState = contentState;
        renderState.breakingContentParts.clear();
        if (!contentState.isAir()) {
            blockModelResolver.update(renderState.content, contentState, CONTENT_BLOCK_DISPLAY_CONTEXT);
            final var blockModel = Minecraft.getInstance().getModelManager().getBlockStateModelSet().get(contentState);
            blockModel.collectParts(renderState.content.scratchRandomSource(contentState.getSeed(blockEntity.getBlockPos())), renderState.breakingContentParts);
        } else {
            renderState.content.clear();
        }
    }

    @Override
    public AutoHammerRenderState createRenderState() {
        return new AutoHammerRenderState();
    }

    @Override
    public void submit(AutoHammerRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.skip) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.rotate(renderState.facing.getRotation());
        poseStack.rotateDegrees(Axis.YP, -90);

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.pushPose();
        poseStack.translate(-0.7f, -0.3f, 0f);
        poseStack.rotateDegrees(Axis.ZP, Math.sin(renderState.hammerAngle) * 30);
        poseStack.translate(-0.4f, 0.2f, 0f);
        renderState.hammerItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();

        if (!renderState.firstHammerItem.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.7f, -0.3f, 0f);
            poseStack.translate(0f, 0.1f, 0.33f);
            poseStack.rotateDegrees(Axis.YP, 10f);
            poseStack.rotateDegrees(Axis.ZP, Math.sin(renderState.hammerAngle - 8f) * 30);
            poseStack.translate(-0.4f, 0.2f, 0f);
            renderState.firstHammerItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (!renderState.secondHammerItem.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.7f, -0.3f, 0f);
            poseStack.translate(0f, 0.1f, -0.33f);
            poseStack.rotateDegrees(Axis.YP, -10);
            poseStack.rotateDegrees(Axis.ZP, Math.sin(renderState.hammerAngle + 8f) * 30);
            poseStack.translate(-0.4f, 0.2f, 0f);
            renderState.secondHammerItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        poseStack.popPose();

        if (!renderState.content.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.4625f, -0.04f, -0.2);
            poseStack.scale(0.4f, 0.4f, 0.4f);
            renderState.content.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);

            if (renderState.progress > 0f && renderState.contentState != null) {
                final var blockDamage = Math.min(9, (int) (renderState.progress * 10f));
                submitNodeCollector.submitBreakingBlockModel(poseStack, renderState.breakingContentParts, blockDamage, false);
            }

            poseStack.popPose();
        }

        poseStack.popPose();
    }

    public static <T extends AutoHammerBlockEntity> BlockEntityRenderer<T, AutoHammerRenderState> normal(BlockEntityRendererProvider.Context context) {
        return (BlockEntityRenderer<T, AutoHammerRenderState>) new AutoHammerRenderer(context, false);
    }

    public static <T extends AutoHammerBlockEntity> BlockEntityRenderer<T, AutoHammerRenderState> compressed(BlockEntityRendererProvider.Context context) {
        return (BlockEntityRenderer<T, AutoHammerRenderState>) new AutoHammerRenderer(context, true);
    }

}
