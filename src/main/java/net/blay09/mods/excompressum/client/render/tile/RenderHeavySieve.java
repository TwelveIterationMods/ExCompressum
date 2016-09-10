package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.StupidUtils;
import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistryEntry;
import net.blay09.mods.excompressum.tile.TileHeavySieve;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class RenderHeavySieve extends TileEntitySpecialRenderer<TileHeavySieve> {

    private ItemStack defaultMesh;

    @Override
    public void renderTileEntityAt(TileHeavySieve tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if(defaultMesh == null) {
            defaultMesh = new ItemStack(ModItems.ironMesh);
        }
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        // Render mesh
        if(ExRegistro.doMeshesHaveDurability()) {
            ItemStack meshStack = tileEntity.getMeshStack();
            if (meshStack != null) {
                SieveMeshRegistryEntry sieveMesh = SieveMeshRegistry.getEntry(meshStack);
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
                }
            }
        }

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (currentStack != null) {
            IBlockState state = StupidUtils.getStateFromItemStack(currentStack);
            //noinspection ConstantConditions /// Forge needs @Nullable
            if(state != null) {
                float progress = tileEntity.getProgress();
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.pushMatrix();
                GlStateManager.translate(0.0625f, 0.5625f, 0.0625f);
                float tt = 0.42f;
                GlStateManager.scale(0.88f, tt - progress * tt, 0.88f);
                mc.getBlockRendererDispatcher().renderBlock(state, new BlockPos(0, 0, 0), tileEntity.getWorld(), renderer);
                tessellator.draw();
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }

}
