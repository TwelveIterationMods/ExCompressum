package net.blay09.mods.excompressum.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Random;

// Blatantly stolen from LibX.
// Apache License 2.0
public class BlockRenderUtils {

    private static final RenderType RENDER_TYPE_BREAK = RenderType.crumbling(InventoryMenu.BLOCK_ATLAS);
    private static final RandomSource random = RandomSource.create();

    /**
     * Renders the break effect for a block state.
     *
     * @param breakProgress How much the block already broke. 0 means no break. This should not be lower than 0 and not be greater than 10.
     */
    public static void renderBlockBreak(BlockState state, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, int breakProgress) {
        renderBlockBreak(state, poseStack, buffer, light, overlay, breakProgress, state.getSeed(BlockPos.ZERO));
    }

    /**
     * Renders the break effect for a block state.
     *
     * @param breakProgress How much the block already broke. 0 means no break. This should not be lower than 0 and not be greater than 10.
     * @param positionRandom The long value to randomize the position. This can be obtained via {@code BlockState#getPositionRandom}.
     */
    public static void renderBlockBreak(BlockState state, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay, int breakProgress, long positionRandom) {
        if (state.getRenderShape() == RenderShape.MODEL && breakProgress > 0) {
            ResourceLocation tex = ModelBakery.DESTROY_STAGES.get((breakProgress - 1) % ModelBakery.DESTROY_STAGES.size());
            BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getBlockModel(state);

            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(tex);
            VertexConsumer vertex = Minecraft.getInstance().renderBuffers().crumblingBufferSource().getBuffer(RENDER_TYPE_BREAK);

            for (Direction direction : Direction.values()) {
                random.setSeed(positionRandom);
                List<BakedQuad> list = model.getQuads(state, direction, random);
                if (!list.isEmpty()) {
                    renderBlockBreakQuad(poseStack.last(), vertex, list, light, overlay, sprite);
                }
            }

            random.setSeed(positionRandom);
            List<BakedQuad> list = model.getQuads(state, null, random);
            if (!list.isEmpty()) {
                renderBlockBreakQuad(poseStack.last(), vertex, list, light, overlay, sprite);
            }
        }
    }

    private static void renderBlockBreakQuad(PoseStack.Pose pose, VertexConsumer vertex, List<BakedQuad> list, int light, int overlay, TextureAtlasSprite sprite) {
        for (BakedQuad quad : list) {
            BakedQuad modifiedQuad = new BakedQuad(modifyBlockBreakQuadData(quad.getVertices(), quad.getSprite(), sprite),
                    quad.getTintIndex(),
                    quad.getDirection(),
                    sprite,
                    quad.isShade());
            vertex.putBulkData(pose, modifiedQuad, 1, 1, 1, light, overlay);
        }
    }

    private static int[] modifyBlockBreakQuadData(int[] data, TextureAtlasSprite oldSprite, TextureAtlasSprite newSprite) {
        // Only works for DefaultVertexFormats.BLOCK, might need to be fixed in the future
        int[] newData = new int[data.length];
        System.arraycopy(data, 0, newData, 0, data.length);
        for (int off = 0; off + 7 < newData.length; off += DefaultVertexFormat.BLOCK.getIntegerSize()) {
            newData[off + 4] = Float.floatToRawIntBits(((Float.intBitsToFloat(data[off + 4]) - oldSprite.getU0()) * newSprite.contents()
                    .width() / oldSprite.contents().width()) + newSprite.getU0());
            newData[off + 5] = Float.floatToRawIntBits(((Float.intBitsToFloat(data[off + 5]) - oldSprite.getV0()) * newSprite.contents()
                    .height() / oldSprite.contents().height()) + newSprite.getV0());
        }
        return newData;
    }

}
