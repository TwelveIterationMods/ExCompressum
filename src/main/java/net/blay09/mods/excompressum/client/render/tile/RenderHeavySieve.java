package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.tile.HeavySieveTileEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.lwjgl.opengl.GL11;

public class RenderHeavySieve extends TileEntityRenderer<HeavySieveTileEntity> {

    public RenderHeavySieve(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(HeavySieveTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Minecraft mc = Minecraft.getInstance();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();

        matrixStack.push();

        matrixStack.push();
        matrixStack.translate(0.5, 0.5, 0.5);
        mc.getItemRenderer().renderItem(new ItemStack(Items.APPLE), ItemCameraTransforms.TransformType.FIXED, combinedLightIn, OverlayTexture.NO_OVERLAY, matrixStack, bufferIn);
        matrixStack.pop();

        // Render mesh
        ItemStack meshStack = tileEntity.getMeshStack();
        if (!meshStack.isEmpty()) {
            /* TODO SieveMeshRegistryEntry sieveMesh = SieveMeshRegistry.getEntry(meshStack);
            if (sieveMesh != null) {
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                TextureAtlasSprite sprite = sieveMesh.getSpriteLocation() != null ? mc.getTextureMapBlocks().getTextureExtry(sieveMesh.getSpriteLocation().toString()) : null;
                if (sprite == null) {
                    sprite = mc.getTextureMapBlocks().getMissingSprite();
                }
                int brightness = tileEntity.getWorld().getCombinedLight(tileEntity.getPos().up(), 0);
                float meshXZ = 0.0625f;
                float meshXZ2 = 1f - meshXZ;
                float meshY = 0.56f;
                RenderUtils.renderQuadUp(renderer, meshXZ, meshY, meshXZ, meshXZ2, meshY, meshXZ2, 0xFFFFFFFF, brightness, sprite);
                tessellator.draw();
            }*/
        }

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            BlockState state = StupidUtils.getStateFromItemStack(currentStack);
            if (state != null) {
                float progress = tileEntity.getProgress();
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                mc.textureManager.bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
                matrixStack.push();
                matrixStack.translate(0.0625f, 0.5625f, 0.0625f);
                float tt = 0.42f;
                matrixStack.scale(0.88f, tt - progress * tt, 0.88f);
                RenderUtils.renderBlockWithTranslate(mc, state, tileEntity.getWorld(), tileEntity.getPos(), renderer);
                tessellator.draw();
                matrixStack.pop();
            }
        }

        matrixStack.pop();

        RenderHelper.enableStandardItemLighting();
    }

}
