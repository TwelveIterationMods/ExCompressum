package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.tile.HeavySieveTileEntity;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

public class HeavySieveRenderer extends TileEntityRenderer<HeavySieveTileEntity> {

    public HeavySieveRenderer(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(HeavySieveTileEntity tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();

        matrixStack.push();

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
            BlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if (contentState != null) {
                float progress = tileEntity.getProgress();
                matrixStack.push();
                matrixStack.translate(0.0625f, 0.5625f, 0.0625f);
                float tt = 0.42f;
                matrixStack.scale(0.88f, tt - progress * tt, 0.88f);
                dispatcher.renderBlock(contentState, matrixStack, buffer, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);
                matrixStack.pop();
            }
        }

        matrixStack.pop();
    }

}
