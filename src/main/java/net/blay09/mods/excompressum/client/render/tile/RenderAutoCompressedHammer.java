package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.block.BlockAutoCompressedHammer;
import net.blay09.mods.excompressum.client.render.model.ModelAutoCompressedHammer;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBlaze;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderAutoCompressedHammer extends TileEntitySpecialRenderer {

    private final ResourceLocation texture = new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/auto_compressed_hammer.png");
    private final ResourceLocation[] destroyStages = new ResourceLocation[] {
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_0"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_1"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_2"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_3"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_4"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_5"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_6"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_7"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_8"),
        new ResourceLocation("minecraft", "textures/blocks/destroy_stage_9")
    };
    private final ModelAutoCompressedHammer model = new ModelAutoCompressedHammer();

    private EntityItem renderItem;

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if(renderItem == null && tileEntity.hasWorldObj()) {
            renderItem = new EntityItem(tileEntity.getWorldObj());
            renderItem.setEntityItemStack(new ItemStack(ModItems.compressedHammerDiamond));
        }

        TileEntityAutoCompressedHammer tileEntityHammer = (TileEntityAutoCompressedHammer) tileEntity;

        GL11.glPushMatrix();
        boolean oldRescaleNormal = GL11.glIsEnabled(GL12.GL_RESCALE_NORMAL);
        if(!oldRescaleNormal) {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glTranslatef((float) x + 0.5f, (float) y, (float) z + 0.5f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        model.renderSolid();
        GL11.glEnable(GL11.GL_BLEND);
        model.renderGlass();
        GL11.glDisable(GL11.GL_BLEND);

        if(tileEntityHammer.getCurrentStack() != null) {
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            GL11.glTranslatef(-0.2f, 0.2f, -0.5f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            renderContent(Block.getBlockFromItem(tileEntityHammer.getCurrentStack().getItem()), tileEntityHammer.getCurrentStack().getItemDamage(), tileEntityHammer.getProgress());
            GL11.glPopMatrix();
        }

        if(tileEntity.hasWorldObj()) {
            RenderItem.renderInFrame = true;
            GL11.glRotatef((float) Math.sin(tileEntityHammer.getProgress() * 100) * 15, 0, 0, 1);
            RenderManager.instance.renderEntityWithPosYaw(renderItem, -0.1, 0.4, 0, 0f, 0f);
            RenderManager.instance.renderEntityWithPosYaw(renderItem, -0.1, 0.4, -0.3, 0f, 0f);
            RenderManager.instance.renderEntityWithPosYaw(renderItem, -0.1, 0.4, 0.3, 0f, 0f);
            RenderItem.renderInFrame = false;
        }
        if(!oldRescaleNormal) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    private void renderContent(Block block, int metadata, float progress) {
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setColorRGBA_F(1f, 1f, 1f, 1f);

        renderBlock(block, metadata, null);
        if(progress > 0f) {
            renderBlock(block, metadata, BlockAutoCompressedHammer.destroyStages[(int) (progress * 9f)]);
        }

        Tessellator.instance.draw();
    }

    private void renderBlock(Block block, int metadata, IIcon override) {
        IIcon icon = override != null ? override : block.getIcon(ForgeDirection.UP.ordinal(), metadata);
        Tessellator.instance.addVertexWithUV(1, 1, 1, icon.getMinU(), icon.getMinV());
        Tessellator.instance.addVertexWithUV(1, 1, 0, icon.getMinU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(0, 1, 0, icon.getMaxU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(0, 1, 1, icon.getMaxU(), icon.getMinV());

        icon = override != null ? override : block.getIcon(ForgeDirection.DOWN.ordinal(), metadata);
        Tessellator.instance.addVertexWithUV(0, 0, 1, icon.getMinU(), icon.getMinV());
        Tessellator.instance.addVertexWithUV(0, 0, 0, icon.getMinU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(1, 0, 0, icon.getMaxU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(1, 0, 1, icon.getMaxU(), icon.getMinV());

        icon = override != null ? override : block.getIcon(ForgeDirection.WEST.ordinal(), metadata);
        Tessellator.instance.addVertexWithUV(0, 1, 1, icon.getMinU(), icon.getMinV());
        Tessellator.instance.addVertexWithUV(0, 1, 0, icon.getMinU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(0, 0, 0, icon.getMaxU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(0, 0, 1, icon.getMaxU(), icon.getMinV());

        icon = override != null ? override : block.getIcon(ForgeDirection.EAST.ordinal(), metadata);
        Tessellator.instance.addVertexWithUV(1, 0, 1, icon.getMinU(), icon.getMinV());
        Tessellator.instance.addVertexWithUV(1, 0, 0, icon.getMinU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(1, 1, 0, icon.getMaxU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(1, 1, 1, icon.getMaxU(), icon.getMinV());

        icon = override != null ? override : block.getIcon(ForgeDirection.SOUTH.ordinal(), metadata);
        Tessellator.instance.addVertexWithUV(0, 1, 1, icon.getMinU(), icon.getMinV());
        Tessellator.instance.addVertexWithUV(0, 0, 1, icon.getMinU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(1, 0, 1, icon.getMaxU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(1, 1, 1, icon.getMaxU(), icon.getMinV());

        icon = override != null ? override : block.getIcon(ForgeDirection.NORTH.ordinal(), metadata);
        Tessellator.instance.addVertexWithUV(0, 1, 0, icon.getMinU(), icon.getMinV());
        Tessellator.instance.addVertexWithUV(1, 1, 0, icon.getMinU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(1, 0, 0, icon.getMaxU(), icon.getMaxV());
        Tessellator.instance.addVertexWithUV(0, 0, 0, icon.getMaxU(), icon.getMinV());
    }
}
