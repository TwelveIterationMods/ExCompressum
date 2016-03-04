package net.blay09.mods.excompressum.client.render.tile;

import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveContents;
import exnihilo.blocks.models.ModelSieveMesh;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.blay09.mods.excompressum.client.render.model.ModelAutoFrame;
import net.blay09.mods.excompressum.tile.TileEntityAutoHeavySieve;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderAutoHeavySieve extends TileEntitySpecialRenderer {

    public static final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/auto_heavy_sieve.png");

    private final ModelAutoFrame frame = new ModelAutoFrame();
    private final ModelSieve model = new ModelSieve();
    private final ModelSieveMesh mesh = new ModelSieveMesh();
    private final ModelSieveContents contents = new ModelSieveContents();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        renderTable(tileEntity, x, y, z, f);
        renderMesh(tileEntity, x, y, z, f);
        renderContents(tileEntity, x, y, z, f);
    }

    private void renderTable(TileEntity tileEntity, double x, double y, double z, float f) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glScalef(-1f, -1f, 1f);
        bindSieveTexture(tileEntity.getBlockMetadata());
        model.simpleRender(0.0625f);
        GL11.glPopMatrix();
    }

    private void renderMesh(TileEntity tileEntity, double x, double y, double z, float f) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 0.69f, (float) z + 0.5f);
        bindTexture(TextureMap.locationBlocksTexture);
        mesh.render(BlockHeavySieve.meshIcon);
        GL11.glPopMatrix();
    }

    private void renderContents(TileEntity tileEntity, double x, double y, double z, float f) {
        TileEntityAutoHeavySieve tileEntitySieve = (TileEntityAutoHeavySieve) tileEntity;
        if (tileEntitySieve.isProcessing()) {
            IIcon icon = tileEntitySieve.getCurrentStack().getIconIndex();
            bindTexture(TextureMap.locationBlocksTexture);

            float contentY = ((1f - ((TileEntityAutoHeavySieve) tileEntity).getProgress()) * (0.9f - 0.7f)) + 0.7f;

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5f, (float) y + contentY, (float) z + 0.5f);
            contents.renderTop(icon);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5f, (float) y + 0.70f, (float) z + 0.5f);
            contents.renderBottom(icon);
            GL11.glPopMatrix();
        }
    }

    protected void bindSieveTexture(int metadata) {
        bindTexture(texture);
    }

}
