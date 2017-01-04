package net.blay09.mods.excompressum.client.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;

public class RenderUtils {

	public static void renderQuadUp(VertexBuffer renderer, float x, float y, float z, float x2, float y2, float z2, int color, int brightness, TextureAtlasSprite sprite) {
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
	}

	/**
	 * This is terrible don't use it
	 */
	public static void renderBlockWithTranslate(Minecraft mc, IBlockState state, World world, BlockPos pos, VertexBuffer renderer) {
		GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
		BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
		dispatcher.renderBlock(state, pos, world, renderer);
		try {
			EnumBlockRenderType renderType = state.getRenderType();
			if (renderType == EnumBlockRenderType.MODEL) {
				IBakedModel model = dispatcher.getModelForState(state);
				dispatcher.getBlockModelRenderer().renderModel(world, model, state, pos, renderer, false);
			}
		} catch (Throwable ignored) {
		}
	}

	public static void preBlockDamage() {
		// TODO look at this later for auto hammer. for some reason doing this makes the block inside go half-transparent too
//		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//		GlStateManager.enableBlend();
//		GlStateManager.color(1f, 1f, 1f, 0.5f);
//		GlStateManager.doPolygonOffset(-3f, -3f);
//		GlStateManager.enablePolygonOffset();
//		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
//		GlStateManager.enableAlpha();
//		GlStateManager.pushMatrix();
	}

	public static void postBlockDamage() {
//		GlStateManager.disableAlpha();
//		GlStateManager.doPolygonOffset(0f, 0f);
//		GlStateManager.disablePolygonOffset();
//		GlStateManager.enableAlpha();
//		GlStateManager.depthMask(true);
//		GlStateManager.popMatrix();
	}
}
