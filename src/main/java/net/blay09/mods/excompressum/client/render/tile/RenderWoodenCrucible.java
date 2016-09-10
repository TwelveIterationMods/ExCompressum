package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.tile.TileWoodenCrucible;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderWoodenCrucible extends TileEntitySpecialRenderer<TileWoodenCrucible> {

    @Override
    public void renderTileEntityAt(TileWoodenCrucible tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        ItemStack outputStack = tileEntity.getItemHandler().getStackInSlot(0);
        if(outputStack != null) {
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.0625f, y + 0.2f, z + 0.0625f);
            GlStateManager.scale(0.9375f, 0.75f, 0.9375f);
            mc.getBlockRendererDispatcher().renderBlock(Blocks.CLAY.getDefaultState(), new BlockPos(0, 0, 0), tileEntity.getWorld(), renderer);
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        int solidVolume = tileEntity.getSolidVolume();
        if(solidVolume > 0) {
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.0625f, y + 0.251f, z + 0.0625f);
            GlStateManager.scale(0.9375f, 0.71 * (float) solidVolume / (float) tileEntity.getSolidCapacity(), 0.9375f);
            mc.getBlockRendererDispatcher().renderBlock(Blocks.LEAVES.getDefaultState(), new BlockPos(0, 0, 0), tileEntity.getWorld(), renderer);
            tessellator.draw();
            GlStateManager.popMatrix();
        }

        FluidStack fluidStack = tileEntity.getFluidTank().getFluid();
        if(fluidStack != null) {
            ResourceLocation still = fluidStack.getFluid().getStill(fluidStack);
            int color = fluidStack.getFluid().getColor(fluidStack);
            TextureAtlasSprite sprite = still == null ? null : mc.getTextureMapBlocks().getTextureExtry(still.toString());
            if (sprite == null) {
                sprite = mc.getTextureMapBlocks().getMissingSprite();
            }
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

            GlStateManager.pushMatrix();

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if(Minecraft.isAmbientOcclusionEnabled()) {
                GL11.glShadeModel(GL11.GL_SMOOTH);
            } else {
                GL11.glShadeModel(GL11.GL_FLAT);
            }

            GlStateManager.translate(x, y, z);

            float waterXZ = 0.0625f;
            float waterXZ2 = 1f - waterXZ;
            float waterY = 0.25f + 0.7f * ((float) fluidStack.amount / (float) tileEntity.getFluidTank().getCapacity());
            int brightness = tileEntity.getWorld().getCombinedLight(tileEntity.getPos().offset(EnumFacing.UP), fluidStack.getFluid().getLuminosity(fluidStack));
            RenderUtils.renderQuadUp(renderer, waterXZ, waterY, waterXZ, waterXZ2, waterY, waterXZ2, color, brightness, sprite);
            tessellator.draw();

            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }

        RenderHelper.enableStandardItemLighting();
    }

}
