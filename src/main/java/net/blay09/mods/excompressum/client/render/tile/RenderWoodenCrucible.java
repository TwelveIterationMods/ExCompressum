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
import org.lwjgl.opengl.GL11;

public class RenderWoodenCrucible extends TileEntitySpecialRenderer {

    public static final ResourceLocation[] texture = new ResourceLocation[]{
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_oak.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_spruce.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_birch.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_jungle.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_acacia.png"),
            new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/crucible_darkoak.png")
    };

    private final ModelCrucible model = new ModelCrucible();
    private final ModelCrucibleInternal internal = new ModelCrucibleInternal();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        TileEntityWoodenCrucible tileCrucible = (TileEntityWoodenCrucible) tileEntity;

        // Render Crucible
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
        GL11.glScalef(-1f, -1f, 1f);
        bindTexture(texture[tileEntity.getBlockMetadata()]);
        model.simpleRender(0.0625f);
        GL11.glPopMatrix();

        // Render Solid Content
        if (tileCrucible.hasSolids()) {
            GL11.glPushMatrix();
            float solidRenderVolume = Math.max(0.2f, Math.min(0.95f, (tileCrucible.getSolidVolume() / TileEntityWoodenCrucible.MAX_FLUID)));
            GL11.glTranslatef((float) x + 0.5f, (float) y + solidRenderVolume, (float) z + 0.5f);
            bindTexture(TextureMap.locationBlocksTexture);
            IIcon icon = tileCrucible.getCurrentMeltable().appearance.getIcon(0, tileCrucible.getCurrentMeltable().appearanceMeta);
            internal.render(new Color(tileCrucible.getCurrentMeltable().appearance.getBlockColor()), icon, false);
            GL11.glPopMatrix();
        }

        // Render Fluid Content
        if(tileCrucible.hasFluids()) {
            GL11.glPushMatrix();
            float fluidRenderVolume = Math.max(0.2f, Math.min(0.95f, (tileCrucible.getFluidVolume() / TileEntityWoodenCrucible.MAX_FLUID)));
            GL11.glTranslatef((float) x + 0.5f, (float) y + fluidRenderVolume, (float) z + 0.5f);
            if (tileCrucible.getFluidVolume() > 0) {
                internal.render(new Color(tileCrucible.getFluid().getColor()), tileCrucible.getFluid().getIcon(), true);
            }
            GL11.glPopMatrix();
        }
    }

}
