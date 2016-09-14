package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.utils.StupidUtils;
import net.blay09.mods.excompressum.item.ModItems;
import net.blay09.mods.excompressum.block.BlockAutoHammer;
import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.registry.ExNihiloProvider;
import net.blay09.mods.excompressum.registry.ExRegistro;
import net.blay09.mods.excompressum.tile.TileAutoHammer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

public class RenderAutoHammer extends TileEntitySpecialRenderer<TileAutoHammer> {

    private final boolean isCompressed;
    private ItemStack hammerItemStack;

    public RenderAutoHammer(boolean isCompressed) {
        this.isCompressed = isCompressed;
    }

    @Override
    public void renderTileEntityAt(TileAutoHammer tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if(!tileEntity.hasWorldObj()) {
            return;
        }
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if(!(state.getBlock() instanceof BlockAutoHammer)) {
            return;
        }
        if (hammerItemStack == null) {
            if (isCompressed) {
                hammerItemStack = new ItemStack(ModItems.compressedHammerDiamond);
            } else {
                hammerItemStack = ExRegistro.getNihiloItem(ExNihiloProvider.NihiloItems.HAMMER_DIAMOND);
                if (hammerItemStack == null) {
                    hammerItemStack = new ItemStack(Items.FISH); // This should never happen
                }
            }
        }

        Minecraft mc = Minecraft.getMinecraft();
        RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();

        RenderHelper.disableStandardItemLighting();

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate((float) x + 0.5f, (float) y, (float) z + 0.5f);

        if(tileEntity.shouldAnimate()) {
            tileEntity.hammerAngle += 0.4f * partialTicks;
        }

        // Render the hammers
        GlStateManager.pushMatrix();
        GlStateManager.rotate((float) Math.sin(tileEntity.hammerAngle) * 15, 0f, 0f, 1f);
        GlStateManager.translate(-0.15f, 0.6f, 0f);
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        itemRenderer.renderItem(hammerItemStack, ItemCameraTransforms.TransformType.FIXED);
        ItemStack firstHammer = tileEntity.getUpgradeStack(0);
        if(firstHammer != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0f, 0f, 0.33f);
            GlStateManager.rotate(10f, 0f, 1f, 0f);
            itemRenderer.renderItem(firstHammer, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }
        ItemStack secondHammer = tileEntity.getUpgradeStack(1);
        if(secondHammer != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0f, 0f, -0.33f);
            GlStateManager.rotate(-10f, 0f, 1f, 0f);
            itemRenderer.renderItem(secondHammer, ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();

        RenderHelper.disableStandardItemLighting();

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (currentStack != null) {
            IBlockState contentState = StupidUtils.getStateFromItemStack(currentStack);
            //noinspection ConstantConditions /// Forge needs @Nullable
            if(contentState != null) {
                renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
                mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                GlStateManager.pushMatrix();
                GlStateManager.translate(-0.09375f, 0.0625f, -0.25);
                GlStateManager.scale(0.5, 0.5, 0.5);
                mc.getBlockRendererDispatcher().renderBlock(contentState, new BlockPos(0, 0, 0), tileEntity.getWorld(), renderer);
//                RenderUtils.preBlockDamage(); // TODO look at this later for auto hammer. for some reason doing this makes the block inside go half-transparent too
                mc.getBlockRendererDispatcher().renderBlockDamage(contentState, new BlockPos(0, 0, 0), ClientProxy.destroyBlockIcons[Math.min(9, (int) (tileEntity.getProgress() * 9f))], tileEntity.getWorld());
//                RenderUtils.postBlockDamage();
                tessellator.draw();
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.popMatrix();

        RenderHelper.enableStandardItemLighting();
    }

    private float getRotationAngle(EnumFacing facing) {
        switch(facing) {
            case NORTH:
                return 0;
            case EAST:
                return -90;
            case SOUTH:
                return 180;
            case WEST:
                return 90;
            default:
                return -90;
        }
    }
}
