package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.tile.WoodenCrucibleTileEntity;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class RenderWoodenCrucible extends TileEntityRenderer<WoodenCrucibleTileEntity> {

    public RenderWoodenCrucible(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(WoodenCrucibleTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();
        mc.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);

        ItemStack outputStack = tileEntity.getItemHandler().getStackInSlot(0);
        if (!outputStack.isEmpty()) {
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            matrixStack.push();
            matrixStack.translate(0.0625f, 0.2f, 0.0625f);
            matrixStack.scale(0.875f, 0.75f, 0.875f);
            RenderUtils.renderBlockWithTranslate(mc, Blocks.CLAY.getDefaultState(), tileEntity.getWorld(), tileEntity.getPos(), renderer);
            tessellator.draw();
            matrixStack.pop();
        }

        int solidVolume = tileEntity.getSolidVolume();
        if (solidVolume > 0) {
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            matrixStack.push();
            matrixStack.translate(+0.0625f, 0.251f, 0.0625f);
            matrixStack.scale(0.875f, (float) (0.71 * (float) solidVolume / (float) tileEntity.getSolidCapacity()), 0.875f);
            RenderUtils.renderBlockWithTranslate(mc, Blocks.OAK_LEAVES.getDefaultState(), tileEntity.getWorld(), tileEntity.getPos(), renderer);
            tessellator.draw();
            matrixStack.pop();
        }

        FluidStack fluidStack = tileEntity.getFluidTank().getFluid();
        if (fluidStack != null) {
            /* TODO ResourceLocation still = fluidStack.getFluid().getStill(fluidStack);
            int color = fluidStack.getFluid().getColor(fluidStack);
            TextureAtlasSprite sprite = still == null ? null : mc.getTextureMapBlocks().getTextureExtry(still.toString());
            if (sprite == null) {
                sprite = mc.getTextureMapBlocks().getMissingSprite();
            }
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

            matrixStack.push();

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GL11.glShadeModel(GL11.GL_SMOOTH);
            } else {
                GL11.glShadeModel(GL11.GL_FLAT);
            }

            float waterXZ = 0.0625f;
            float waterXZ2 = 1f - waterXZ;
            float waterY = 0.25f + 0.7f * ((float) fluidStack.getAmount() / (float) tileEntity.getFluidTank().getCapacity());
            int brightness = tileEntity.getWorld().getCombinedLight(tileEntity.getPos().offset(Direction.UP), fluidStack.getFluid().getLuminosity(fluidStack));
            RenderUtils.renderQuadUp(renderer, waterXZ, waterY, waterXZ, waterXZ2, waterY, waterXZ2, color, brightness, sprite);
            tessellator.draw();

            RenderSystem.disableBlend();
            matrixStack.pop();*/
        }

        RenderHelper.enableStandardItemLighting();
    }
}
