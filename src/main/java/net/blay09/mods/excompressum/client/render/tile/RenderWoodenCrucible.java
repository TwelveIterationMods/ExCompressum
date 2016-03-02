package net.blay09.mods.excompressum.client.render.tile;

import exnihilo.blocks.models.ModelCrucible;
import exnihilo.blocks.models.ModelCrucibleInternal;
import exnihilo.registries.helpers.Color;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.TileEntityWoodenCrucible;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import org.lwjgl.opengl.GL11;

public class RenderWoodenCrucible extends TileEntitySpecialRenderer {

    public static final ResourceLocation[] texture = new ResourceLocation[] {
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_oak.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_spruce.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_birch.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_jungle.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_acacia.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_darkoak.png")
    };

    private final ModelCrucible model;
    private final ModelCrucibleInternal internal;

    public RenderWoodenCrucible(ModelCrucible model) {
        this.model = model;
        internal = new ModelCrucibleInternal();
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        drawCrucible(tileEntity, x, y, z, f);
        drawContents(tileEntity, x, y, z, f);
    }

    private void drawCrucible(TileEntity tileEntity, double x, double y, double z, float f) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glScalef(-1f, -1f, 1f);
        bindTexture(texture[tileEntity.getBlockMetadata()]);
        model.simpleRender(0.0625F);
        GL11.glPopMatrix();
    }

    private void drawContents(TileEntity tileEntity, double x, double y, double z, float f) {
        TileEntityWoodenCrucible tileCrucible = (TileEntityWoodenCrucible) tileEntity;
        GL11.glPushMatrix();
        float solidRenderVolume = Math.max(0.2f, Math.min(0.95f, ((float) tileCrucible.getSolidVolume() / TileEntityWoodenCrucible.MAX_FLUID)));
        GL11.glTranslatef((float) x + 0.5f, (float) y + solidRenderVolume, (float) z + 0.5f);
        bindTexture(TextureMap.locationBlocksTexture);
        if(tileCrucible.hasSolids()) {
            renderSolid(tileCrucible);
        }
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        float fluidRenderVolume = Math.max(0.2f, Math.min(0.95f, ((float) tileCrucible.getFluidVolume() / TileEntityWoodenCrucible.MAX_FLUID)));
        GL11.glTranslatef((float) x + 0.5f, (float) y + fluidRenderVolume, (float) z + 0.5f);
        if(tileCrucible.getFluidVolume() > 0) {
            renderFluid(tileCrucible);
        }
        GL11.glPopMatrix();
    }

    private void renderSolid(TileEntityWoodenCrucible tileEntity) {
        IIcon icon = tileEntity.getContent().appearance.getIcon(0, tileEntity.getContent().appearanceMeta);
        internal.render(new Color(tileEntity.getContent().appearance.getBlockColor()), icon, false);
    }

    private void renderFluid(TileEntityWoodenCrucible tileEntity) {
        Fluid content = tileEntity.getFluidStack().getFluid();
        internal.render(new Color(content.getColor()), content.getIcon(), true);
    }

}
