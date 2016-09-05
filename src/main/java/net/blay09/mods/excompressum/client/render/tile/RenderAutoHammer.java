package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.tile.TileAutoHammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class RenderAutoHammer extends TileEntitySpecialRenderer<TileAutoHammer> {

    private final ItemStack hammerItemStack;
    private EntityItem renderItem;

    public RenderAutoHammer(ItemStack hammerItemStack) {
        this.hammerItemStack = hammerItemStack;
    }

    @Override
    public void renderTileEntityAt(TileAutoHammer tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if(renderItem == null) {
            if(tileEntity.hasWorldObj()) {
                renderItem = new EntityItem(tileEntity.getWorld());
            } else {
                renderItem = new EntityItem(Minecraft.getMinecraft().theWorld);
            }
            renderItem.setEntityItemStack(hammerItemStack);
        }

        int metadata = tileEntity.hasWorldObj() ? tileEntity.getBlockMetadata() : 0;

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal(); // TODO why, what is this
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

        if(tileEntity.getCurrentStack() != null) {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5f, 0.5f, 0.5f);
            GlStateManager.translate(-0.2f, 0.2f, -0.5f);
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            // TODO hi fix me
//            renderContent(Block.getBlockFromItem(tileEntity.getCurrentStack().getItem()), tileEntity.getCurrentStack().getItemDamage(), tileEntity.getProgress());
            GlStateManager.popMatrix();
        }

        if(renderItem != null) {
            GlStateManager.rotate((float) Math.sin(tileEntity.getProgress() / tileEntity.getSpeedBoost() * 100) * 15, 0, 0, 1);
            // TODO check CfB for item render code
//            RenderItem.renderInFrame = true;
//            RenderManager.instance.renderEntityWithPosYaw(renderItem, -0.1, 0.4, 0, 0f, 0f);
            if(tileEntity.getUpgradeStack(0) != null) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(-15, 0f, 1f, 0f);
//                RenderManager.instance.renderEntityWithPosYaw(renderItem, -0.1, 0.4, -0.3, 0f, 0f);
                GlStateManager.popMatrix();
            }
            if(tileEntity.getUpgradeStack(1) != null) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(15, 0f, 1f, 0f);
//                RenderManager.instance.renderEntityWithPosYaw(renderItem, -0.1, 0.4, 0.3, 0f, 0f);
                GlStateManager.popMatrix();
            }
//            RenderItem.renderInFrame = false;
        }
        GlStateManager.enableRescaleNormal(); // TODO again, why, wat dis
        GlStateManager.popMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    /* TODO fix this too
    private void renderContent(Block block, int metadata, float progress) {
        Tessellator.instance.startDrawingQuads();
        Tessellator.instance.setColorRGBA_F(1f, 1f, 1f, 1f);

        renderBlock(block, metadata, null);
        if(progress > 0f) {
            renderBlock(block, metadata, BlockAutoCompressedHammer.destroyStages[(int) (progress * 9f)]);
        }

        Tessellator.instance.draw();
    }*/

    // TODO yeah fix this
    /*private void renderBlock(Block block, int metadata, IIcon override) {
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
    }*/

}
