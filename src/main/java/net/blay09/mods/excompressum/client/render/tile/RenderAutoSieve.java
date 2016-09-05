package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveContents;
import exnihilo.blocks.models.ModelSieveMesh;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.BlockHeavySieve;
import net.blay09.mods.excompressum.client.render.model.ModelAutoFrame;
import net.blay09.mods.excompressum.client.render.model.ModelTinyHuman;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieveBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.Map;

public class RenderAutoSieve extends TileEntitySpecialRenderer {

    private static final ResourceLocation textureFrame = new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/auto_frame.png");
    private static final ResourceLocation textureSieve = new ResourceLocation("exnihilo", "textures/blocks/ModelSieveOak.png");

    private final ModelAutoFrame frame = new ModelAutoFrame();
    private final ModelTinyHuman biped = new ModelTinyHuman();
    private final ModelSieve sieve = new ModelSieve();
    private final ModelSieveMesh mesh = new ModelSieveMesh();
    private final ModelSieveContents contents = new ModelSieveContents();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        TileEntityAutoSieveBase tileEntitySieve = (TileEntityAutoSieveBase) tileEntity;
        int metadata = tileEntity.hasWorldObj() ? tileEntity.getBlockMetadata() : 0;
        GL11.glPushMatrix();
        boolean oldRescaleNormal = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
        if (!oldRescaleNormal) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);

        float angle;
        switch(ForgeDirection.getOrientation(metadata)) {
            case NORTH:
                angle = 0;
                break;
            case EAST:
                angle = -90;
                break;
            case SOUTH:
                angle = 180;
                break;
            case WEST:
                angle = 90;
                break;
            default:
                angle = -90;
        }
        GL11.glRotatef(angle, 0f, 1f, 0f);

        bindFrameTexture();
        frame.renderSolid();
        GL11.glEnable(GL11.GL_BLEND);
        frame.renderGlass();
        GL11.glDisable(GL11.GL_BLEND);

        GL11.glPushMatrix();
        GL11.glRotatef(90, 0, 1, 0);
        GL11.glRotatef(180, 1, 0, 0);
        GL11.glTranslatef(0, -1.2f, 0.25f);
        GL11.glScalef(0.75f, 0.75f, 0.75f);
        bindPlayerTexture(tileEntitySieve.getCustomSkin());
        biped.renderAll(tileEntitySieve.isActive(), tileEntitySieve.getSpeedBoost());
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glScalef(-0.4f, -0.4f, -0.4f);
        GL11.glTranslatef(-0.35f, -1.75f, 0);
        bindSieveTexture();
        sieve.simpleRender(0.0625f);
        GL11.glTranslatef(0, 0.8f, 0);
        GL11.glScalef(0.87f, 0.87f, 0.87f);
        bindTexture(TextureMap.locationBlocksTexture);
        mesh.render(BlockHeavySieve.meshIcon);
        if (tileEntitySieve.getCurrentStack() != null) {
            IIcon icon = tileEntitySieve.getCurrentStack().getIconIndex();
            float contentY = ((1f - tileEntitySieve.getProgress()) * (0.9f - 0.7f)) + 0.7f;
            GL11.glTranslatef(0, -contentY + 0.7f, 0);
            contents.renderTop(icon);
            contents.renderBottom(icon);
        }
        GL11.glPopMatrix();

        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    private void bindPlayerTexture(GameProfile customSkin) {
        ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;
        if (customSkin != null) {
            Map map = Minecraft.getMinecraft().func_152342_ad().func_152788_a(customSkin);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                resourcelocation = Minecraft.getMinecraft().func_152342_ad().func_152792_a((MinecraftProfileTexture) map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            }
        }
        bindTexture(resourcelocation);
    }

    protected void bindFrameTexture() {
        bindTexture(textureFrame);
    }

    protected void bindSieveTexture() {
        bindTexture(textureSieve);
    }

}
