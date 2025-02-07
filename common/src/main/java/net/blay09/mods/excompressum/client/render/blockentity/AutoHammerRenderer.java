package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.client.render.BlockRenderUtils;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.tag.ModItemTags;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.AxisAngle4f;
import org.joml.Math;
import org.joml.Quaternionf;

public class AutoHammerRenderer implements BlockEntityRenderer<AutoHammerBlockEntity> {

    private final boolean isCompressed;

    private ItemStack hammerItemStack = ItemStack.EMPTY;

    public AutoHammerRenderer(BlockEntityRendererProvider.Context context, boolean isCompressed) {
        this.isCompressed = isCompressed;
    }

    @Override
    public void render(AutoHammerBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int combinedLight, int combinedOverlay) {
        final var level = tileEntity.getLevel();
        if (level == null) {
            return;
        }

        if (tileEntity.isUgly()) {
            return;
        }

        if (hammerItemStack.isEmpty()) {
            if (isCompressed) {
                hammerItemStack = new ItemStack(ModItems.compressedDiamondHammer);
            } else {
                for (final var itemHolder : BuiltInRegistries.ITEM.getTagOrEmpty(ModItemTags.DIAMOND_HAMMERS)) {
                    hammerItemStack = new ItemStack(itemHolder.value());
                    break;
                }
                if (hammerItemStack.isEmpty()) {
                    hammerItemStack = new ItemStack(Items.COD); // This should never happen
                }
            }
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(tileEntity.getFacing().getRotation());
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(-90), 0f, 1f, 0f)));

        if (tileEntity.shouldAnimate()) {
            tileEntity.hammerAngle += 0.4f * partialTicks;
        }

        // Render the hammers
        poseStack.pushPose();
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.pushPose();
        poseStack.translate(-0.7f, -0.3f, 0f);
        poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(Math.sin(tileEntity.hammerAngle) * 30), 0, 0, 1f)));
        poseStack.translate(-0.4f, 0.2f, 0f);
        itemRenderer.renderStatic(hammerItemStack, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffers, level, 0);
        poseStack.popPose();

        ItemStack firstHammer = tileEntity.getUpgradeStack(0);
        if (!firstHammer.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.7f, -0.3f, 0f);
            poseStack.translate(0f, 0.1f, 0.33f);
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(10f), 0f, 1, 0)));
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(Math.sin(tileEntity.hammerAngle - 8f) * 30), 0, 0, 1f)));
            poseStack.translate(-0.4f, 0.2f, 0f);
            itemRenderer.renderStatic(firstHammer, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffers, level, 0);
            poseStack.popPose();
        }

        ItemStack secondHammer = tileEntity.getUpgradeStack(1);
        if (!secondHammer.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(-0.7f, -0.3f, 0f);
            poseStack.translate(0f, 0.1f, -0.33f);
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(-10), 0f, 1f, 0)));
            poseStack.mulPose(new Quaternionf(new AxisAngle4f(Math.toRadians(Math.sin(tileEntity.hammerAngle + 8f) * 30), 0, 0, 1f)));
            poseStack.translate(-0.4f, 0.2f, 0f);
            itemRenderer.renderStatic(secondHammer, ItemDisplayContext.FIXED, combinedLight, combinedOverlay, poseStack, buffers, level, 0);
            poseStack.popPose();
        }

        poseStack.popPose();

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (!contentState.isAir()) {
                poseStack.pushPose();
                poseStack.translate(-0.4625f, -0.04f, -0.2);
                poseStack.scale(0.4f, 0.4f, 0.4f);
                BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                dispatcher.renderSingleBlock(contentState, poseStack, buffers, combinedLight, combinedOverlay);

                if (tileEntity.getProgress() > 0f) {
                    int blockDamage = Math.min(9, (int) (tileEntity.getProgress() * 9f));
                    BlockRenderUtils.renderBlockBreak(contentState, poseStack, buffers, combinedLight, combinedOverlay, blockDamage + 1);
                }

                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

    public static <T extends AutoHammerBlockEntity> BlockEntityRenderer<T> normal(BlockEntityRendererProvider.Context context) {
        return (BlockEntityRenderer<T>) new AutoHammerRenderer(context, false);
    }

    public static <T extends AutoHammerBlockEntity> BlockEntityRenderer<T> compressed(BlockEntityRendererProvider.Context context) {
        return (BlockEntityRenderer<T>) new AutoHammerRenderer(context, true);
    }

}
