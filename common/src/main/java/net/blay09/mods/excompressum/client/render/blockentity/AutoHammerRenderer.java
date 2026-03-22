package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.block.AutoHammerBlock;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.minecraft.client.renderer.SubmitNodeCollector;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Quaternionf;

public class AutoHammerRenderer implements BlockEntityRenderer<AutoHammerBlockEntity, AutoHammerRenderer.AutoHammerRenderState> {

    public static class AutoHammerRenderState extends BlockEntityRenderState {
        public boolean skip;
        public Direction facing = Direction.NORTH;
        public float hammerAngle;
        public final ItemStackRenderState hammerItem = new ItemStackRenderState();
        public final ItemStackRenderState firstHammerItem = new ItemStackRenderState();
        public final ItemStackRenderState secondHammerItem = new ItemStackRenderState();
        public final ItemStackRenderState item = new ItemStackRenderState();
        public float progress;
    }

    private final ItemModelResolver itemModelResolver;
    private final boolean isCompressed;

    private ItemStack hammerItemStack = ItemStack.EMPTY;

    public AutoHammerRenderer(BlockEntityRendererProvider.Context context, boolean isCompressed) {
        this.itemModelResolver = context.itemModelResolver();
        this.isCompressed = isCompressed;
    }

    @Override
    public void extractRenderState(AutoHammerBlockEntity blockEntity, AutoHammerRenderState renderState, float delta, Vec3 vec, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
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
        itemModelResolver.updateForTopItem(renderState.item, blockEntity.getCurrentStack(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
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
        poseStack.mulPose(renderState.facing.getRotation());
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(-90), 0f, 1f, 0f)));

        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.pushPose();
        poseStack.translate(-0.7f, -0.3f, 0f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(Math.sin(renderState.hammerAngle) * 30), 0, 0, 1f)));
        poseStack.translate(-0.4f, 0.2f, 0f);
        renderState.hammerItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
        poseStack.popPose();

        if (!renderState.firstHammerItem.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.7f, -0.3f, 0f);
            poseStack.translate(0f, 0.1f, 0.33f);
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(10f), 0f, 1, 0)));
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(Math.sin(renderState.hammerAngle - 8f) * 30), 0, 0, 1f)));
            poseStack.translate(-0.4f, 0.2f, 0f);
            renderState.firstHammerItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        if (!renderState.secondHammerItem.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.7f, -0.3f, 0f);
            poseStack.translate(0f, 0.1f, -0.33f);
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(-10), 0f, 1f, 0)));
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(Math.sin(renderState.hammerAngle + 8f) * 30), 0, 0, 1f)));
            poseStack.translate(-0.4f, 0.2f, 0f);
            renderState.secondHammerItem.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);
            poseStack.popPose();
        }

        poseStack.popPose();

        if (!renderState.item.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.4625f, -0.04f, -0.2);
            poseStack.scale(0.4f, 0.4f, 0.4f);
            renderState.item.submit(poseStack, submitNodeCollector, renderState.lightCoords, OverlayTexture.NO_OVERLAY, 0);

            if (renderState.progress > 0f) {
                // TODO int blockDamage = Math.min(9, (int) (renderState.progress * 9f));
                // TODO final var crumblingBufferSource = Minecraft.getInstance().renderBuffers().crumblingBufferSource();
                // TODO final var crumblingBuffer = crumblingBufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(blockDamage));
                // TODO final var vertexConsumer = new SheetedDecalTextureGenerator(crumblingBuffer, poseStack.last(), 1f);
                // TODO dispatcher.renderBreakingTexture(contentState, tileEntity.getBlockPos(), level, poseStack, vertexConsumer);
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
