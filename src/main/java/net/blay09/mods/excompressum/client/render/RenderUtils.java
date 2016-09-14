package net.blay09.mods.excompressum.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

	public static void renderQuadUp(VertexBuffer renderer, float x, float y, float z, float x2, float y2, float z2, int color, int brightness, TextureAtlasSprite sprite) {
		float d = 0.005f;
		float d2 = 1 - (d * 2);
		double minU = sprite.getInterpolatedU(d%1d * 16f);
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

	public static void preBlockDamage() {
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.DST_COLOR, GlStateManager.DestFactor.SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.enableBlend();
		GlStateManager.color(1f, 1f, 1f, 0.5f);
		GlStateManager.doPolygonOffset(-3f, -3f);
		GlStateManager.enablePolygonOffset();
		GlStateManager.alphaFunc(GL11.GL_GREATER, 0.1f);
		GlStateManager.enableAlpha();
		GlStateManager.pushMatrix();
	}

	public static void postBlockDamage() {
		GlStateManager.disableAlpha();
		GlStateManager.doPolygonOffset(0f, 0f);
		GlStateManager.disablePolygonOffset();
		GlStateManager.enableAlpha();
		GlStateManager.depthMask(true);
		GlStateManager.popMatrix();
	}
}
