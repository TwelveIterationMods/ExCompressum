package net.blay09.mods.excompressum.client;

import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveContents;
import exnihilo.blocks.models.ModelSieveMesh;
import exnihilo.blocks.tileentities.TileEntitySieve;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.blay09.mods.excompressum.tile.TileEntityHeavySieve;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderHeavySieve extends TileEntitySpecialRenderer {

    public static final ResourceLocation[] textures = new ResourceLocation[] {
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/heavy_sieve_oak.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/heavy_sieve_spruce.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/heavy_sieve_birch.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/heavy_sieve_jungle.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/heavy_sieve_acacia.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/heavy_sieve_darkoak.png")
    };

    private final ModelSieve model;
    private final ModelSieveMesh mesh;
    private final ModelSieveContents contents;

    public RenderHeavySieve(ModelSieve model, ModelSieveMesh mesh) {
        this.model = model;
        this.mesh = mesh;
        this.contents = new ModelSieveContents();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float f) {
        renderTable(tileentity, x, y, z, f);
        renderMesh(tileentity, x, y, z, f);
        renderContents(tileentity, x, y, z, f);
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
        TileEntityHeavySieve tileEntitySieve = (TileEntityHeavySieve) tileEntity;
        if (tileEntitySieve.getMode() == TileEntitySieve.SieveMode.FILLED) {
            IIcon icon = tileEntitySieve.getContent().getIconIndex();
            bindTexture(TextureMap.locationBlocksTexture);

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5f, (float) y + tileEntitySieve.getAdjustedVolume(), (float) z + 0.5f);
            contents.renderTop(icon);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5f, (float) y + 0.70f, (float) z + 0.5f);
            contents.renderBottom(icon);
            GL11.glPopMatrix();
        }
    }

    private void bindSieveTexture(int metadata) {
        if (metadata >= 0) {
            bindTexture(textures[metadata]);
        }
    }
}
