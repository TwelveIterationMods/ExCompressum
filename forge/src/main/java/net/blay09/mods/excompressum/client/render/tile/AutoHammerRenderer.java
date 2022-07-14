package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.block.entity.AbstractAutoSieveBlockEntity;
import net.blay09.mods.excompressum.client.render.BlockRenderUtils;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExNihilo;
import net.blay09.mods.excompressum.block.entity.AutoHammerBlockEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class AutoHammerRenderer implements BlockEntityRenderer<AutoHammerBlockEntity> {

    private final boolean isCompressed;

    private ItemStack hammerItemStack = ItemStack.EMPTY;

    public AutoHammerRenderer(BlockEntityRendererProvider.Context context, boolean isCompressed) {
        this.isCompressed = isCompressed;
    }

    @Override
    public void render(AutoHammerBlockEntity tileEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int combinedLight, int combinedOverlay) {
        if (!tileEntity.hasLevel() || tileEntity.isUgly()) {
            return;
        }

        if (hammerItemStack.isEmpty()) {
            if (isCompressed) {
                hammerItemStack = new ItemStack(ModItems.compressedHammerDiamond);
            } else {
                hammerItemStack = ExNihilo.getInstance().getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
                if (hammerItemStack.isEmpty()) {
                    hammerItemStack = new ItemStack(Items.COD); // This should never happen
                }
            }
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        poseStack.pushPose();
        poseStack.translate(0.5f, 0f, 0.5f);
        poseStack.mulPose(new Quaternion(0f, RenderUtils.getRotationAngle(tileEntity.getFacing()), 0f, true));

        if (tileEntity.shouldAnimate()) {
            tileEntity.hammerAngle += 0.4f * partialTicks;
        }

        // Render the hammers
        poseStack.pushPose();
        poseStack.mulPose(new Quaternion(0, 180f, 0f, true));
        poseStack.mulPose(new Quaternion(0, 0, (float) Math.sin(tileEntity.hammerAngle) * 15, true));
        poseStack.translate(0.15f, 0.6f, 0f);
        poseStack.scale(0.5f, 0.5f, 0.5f);
        itemRenderer.renderStatic(hammerItemStack, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffers, 0);

        ItemStack firstHammer = tileEntity.getUpgradeStack(0);
        if (!firstHammer.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0f, 0f, 0.33f);
            poseStack.mulPose(new Quaternion(0f, -10, 0, true));
            itemRenderer.renderStatic(firstHammer, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffers, 0);
            poseStack.popPose();
        }

        ItemStack secondHammer = tileEntity.getUpgradeStack(1);
        if (!secondHammer.isEmpty()) {
            poseStack.pushPose();
            poseStack.translate(0f, 0f, -0.33f);
            poseStack.mulPose(new Quaternion(0f, 10, 0, true));
            itemRenderer.renderStatic(secondHammer, ItemTransforms.TransformType.FIXED, combinedLight, combinedOverlay, poseStack, buffers, 0);
            poseStack.popPose();
        }

        poseStack.popPose();

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (!contentState.isAir()) {
                poseStack.pushPose();
                poseStack.translate(-0.09375f, 0.0625f, -0.25);
                poseStack.scale(0.5f, 0.5f, 0.5f);
                BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
                dispatcher.renderSingleBlock(contentState, poseStack, buffers, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);

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
