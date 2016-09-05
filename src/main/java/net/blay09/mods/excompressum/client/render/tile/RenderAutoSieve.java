package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.blay09.mods.excompressum.client.render.model.ModelTinyHuman;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieveBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class RenderAutoSieve extends TileEntitySpecialRenderer<TileEntityAutoSieveBase> {

    private final ModelTinyHuman biped = new ModelTinyHuman();

    @Override
    public void renderTileEntityAt(TileEntityAutoSieveBase tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        int metadata = tileEntity.hasWorldObj() ? tileEntity.getBlockMetadata() : 0;
        GlStateManager.pushMatrix();
//        boolean oldRescaleNormal = GlStateManager.(GL12.GL_RESCALE_NORMAL); // TODO no seriously what is this and why do I care
//        if (!oldRescaleNormal) {
//            GlStateManager.glEnable(GL12.GL_RESCALE_NORMAL);
//        }
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate((float) x + 0.5f, (float) y, (float) z + 0.5f);

        float angle;
        switch(EnumFacing.getFront(metadata)) {
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
        GlStateManager.rotate(angle, 0f, 1f, 0f);

        GlStateManager.pushMatrix();
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, -1.2f, 0.25f);
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
        bindPlayerTexture(tileEntity.getCustomSkin());
        biped.renderAll(tileEntity.isActive(), tileEntity.getSpeedBoost());
        GlStateManager.popMatrix();

        if (tileEntity.getCurrentStack() != null) {
            // TODO yeah the milk jar as usual <3
//            IIcon icon = tileEntity.getCurrentStack().getIconIndex();
//            float contentY = ((1f - tileEntity.getProgress()) * (0.9f - 0.7f)) + 0.7f;
//            GlStateManager.translate(0, -contentY + 0.7f, 0);
//            contents.renderTop(icon);
//            contents.renderBottom(icon);
        }
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
    }

    private void bindPlayerTexture(GameProfile customSkin) {
        ResourceLocation resourceLocation = DefaultPlayerSkin.getDefaultSkinLegacy();
        if (customSkin != null) {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = Minecraft.getMinecraft().getSkinManager().loadSkinFromCache(customSkin);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                resourceLocation = Minecraft.getMinecraft().getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            }
        }
        bindTexture(resourceLocation);
    }

}
