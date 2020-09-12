package net.blay09.mods.excompressum.client.render;


import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RenderUtils {

    public static void renderQuadUp(BufferBuilder renderer, float x, float y, float z, float x2, float y2, float z2, int color, int brightness, TextureAtlasSprite sprite) {
        /* TODO
        float d = 0.005f;
        float d2 = 1 - (d * 2);
        double minU = sprite.getInterpolatedU(d % 1d * 16f);
        double maxU = sprite.getInterpolatedU((d + d2) * 16f);
        double minV = sprite.getInterpolatedV((d % 1d) * 16f);
        double maxV = sprite.getInterpolatedV((d + d2) * 16f);

        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int lightX = brightness >> 0x10 & 0xFFFF;
        int lightZ = brightness & 0xFFFF;
        renderer.pos(x, y, z).color(r, g, b, a).tex(minU, minV).lightmap(lightX, lightZ).endVertex();
        renderer.pos(x, y, z2).color(r, g, b, a).tex(minU, maxV).lightmap(lightX, lightZ).endVertex();
        renderer.pos(x2, y, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(lightX, lightZ).endVertex();
        renderer.pos(x2, y, z).color(r, g, b, a).tex(maxU, minV).lightmap(lightX, lightZ).endVertex();
        */
    }

    /**
     * This is terrible don't use it
     */
    public static void renderBlockWithTranslate(Minecraft mc, BlockState state, World world, BlockPos pos, BufferBuilder renderer) {
        /* TODO
        GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
        try {
            BlockRenderType renderType = state.getRenderType();
            if (renderType == BlockRenderType.MODEL) {
                IBakedModel model = dispatcher.getModelForState(state);
                dispatcher.getBlockModelRenderer().renderModel(world, model, state, pos, renderer, false);
            }
        } catch (Throwable ignored) {
        }
         */
    }

    public static float getRotationAngle(Direction facing) {
        switch (facing) {
            case NORTH:
                return 0;
            case EAST:
                return -90;
            case SOUTH:
                return 180;
            case WEST:
                return 90;
            default:
                return -90;
        }
    }
}
