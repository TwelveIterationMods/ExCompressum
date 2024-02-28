package net.blay09.mods.excompressum.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.blay09.mods.excompressum.api.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.client.ModModels;
import net.blay09.mods.excompressum.block.entity.HeavySieveBlockEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class HeavySieveRenderer implements BlockEntityRenderer<HeavySieveBlockEntity> {

    private static final RandomSource random = RandomSource.create();

    public HeavySieveRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(HeavySieveBlockEntity blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffers, int combinedLight, int combinedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null) {
            return;
        }

        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        poseStack.pushPose();

        // Render mesh
        SieveMeshRegistryEntry mesh = blockEntity.getSieveMesh();
        if (mesh != null) {
            BakedModel meshModel = ModModels.meshes.get(mesh.getModelName()).get();
            if (meshModel != null) {
                dispatcher.getModelRenderer().tesselateBlock(level, meshModel, blockEntity.getBlockState(), blockEntity.getBlockPos(), poseStack, buffers.getBuffer(RenderType.translucent()), false, random, 0, Integer.MAX_VALUE);
            }
        }

        ItemStack currentStack = blockEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (contentState != null) {
                float progress = blockEntity.getProgress();
                poseStack.pushPose();
                poseStack.translate(0.0625f, 0.5625f, 0.0625f);
                float tt = 0.42f;
                poseStack.scale(0.88f, tt - progress * tt, 0.88f);
                dispatcher.renderSingleBlock(contentState, poseStack, buffers, combinedLight, combinedOverlay);
                poseStack.popPose();
            }
        }

        poseStack.popPose();
    }

}
