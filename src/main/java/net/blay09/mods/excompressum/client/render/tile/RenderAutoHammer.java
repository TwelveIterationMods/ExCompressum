package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.client.render.RenderUtils;
import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.block.BlockAutoHammer;
import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.api.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.TileAutoHammer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderAutoHammer extends TileEntitySpecialRenderer<TileAutoHammer> {

    private final boolean isCompressed;
    private ItemStack hammerItemStack = ItemStack.EMPTY;

    public RenderAutoHammer(boolean isCompressed) {
        this.isCompressed = isCompressed;
    }

    @Override
    public void render(TileAutoHammer tileEntity, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if(!tileEntity.hasWorld()) {
            return;
        }
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if(!(state.getBlock() instanceof BlockAutoHammer)) {
            return;
        }
        if (hammerItemStack.isEmpty()) {
            if (isCompressed) {
                hammerItemStack = new ItemStack(ModItems.compressedHammerDiamond);
            } else {
                hammerItemStack = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
                if (hammerItemStack.isEmpty()) {
                    hammerItemStack = new ItemStack(Items.FISH); // This should never happen
                }
            }
        }

        Minecraft mc = Minecraft.getMinecraft();
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate((float) x + 0.5f, (float) y, (float) z + 0.5f);

        if(tileEntity.shouldAnimate()) {
            tileEntity.hammerAngle += 0.4f * partialTicks;
        }

        // Render the hammers
        GlStateManager.pushMatrix();
        GlStateManager.rotate(180f, 0f, 1f, 0f);
        GlStateManager.rotate((float) Math.sin(tileEntity.hammerAngle) * 15, 0f, 0f, 1f);
        GlStateManager.translate(0.15f, 0.6f, 0f);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        itemRenderer.renderItem(hammerItemStack, ItemCameraTransforms.TransformType.FIXED);
        ItemStack firstHammer = tileEntity.getUpgradeStack(0);
        if(!firstHammer.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0f, 0f, 0.33f);
            GlStateManager.rotate(10f, 0f, 1f, 0f);
            itemRenderer.renderItem(firstHammer, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }
        ItemStack secondHammer = tileEntity.getUpgradeStack(1);
        if(!secondHammer.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0f, 0f, -0.33f);
            GlStateManager.rotate(-10f, 0f, 1f, 0f);
            itemRenderer.renderItem(secondHammer, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (!currentStack.isEmpty()) {
            IBlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            if(contentState != null) {
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.pushMatrix();
                GlStateManager.translate(-0.09375f, 0.0625f, -0.25);
                GlStateManager.scale(0.5, 0.5, 0.5);
                RenderUtils.renderBlockWithTranslate(mc, contentState, tileEntity.getWorld(), tileEntity.getPos(), renderer);
                mc.getBlockRendererDispatcher().renderBlockDamage(contentState, tileEntity.getPos(), ClientProxy.destroyBlockIcons[Math.min(9, (int) (tileEntity.getProgress() * 9f))], tileEntity.getWorld());
                tessellator.draw();
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }

}
