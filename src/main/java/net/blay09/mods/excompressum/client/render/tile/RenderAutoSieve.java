package net.blay09.mods.excompressum.client.render.tile;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.block.BlockAutoSieveBase;
import net.blay09.mods.excompressum.client.render.model.ModelTinyHuman;
import net.blay09.mods.excompressum.tile.TileEntityAutoSieveBase;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.Map;

public class RenderAutoSieve extends TileEntitySpecialRenderer<TileEntityAutoSieveBase> {

    private final ModelTinyHuman biped = new ModelTinyHuman();

    @Override
    public void renderTileEntityAt(TileEntityAutoSieveBase tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        if(!tileEntity.hasWorldObj()) {
            return;
        }
        IBlockState state = tileEntity.getWorld().getBlockState(tileEntity.getPos());
        if(!(state.getBlock() instanceof BlockAutoSieveBase)) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();

        GlStateManager.pushMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.translate((float) x + 0.5f, (float) y, (float) z + 0.5f);
        GlStateManager.rotate(getRotationAngle(state.getValue(BlockAutoSieveBase.FACING)), 0f, 1f, 0f);

        // Render the tiny human
        GlStateManager.pushMatrix();
        GlStateManager.rotate(90, 0, 1, 0);
        GlStateManager.rotate(180, 1, 0, 0);
        GlStateManager.translate(0, -1.2f, 0.25f);
        GlStateManager.scale(0.75f, 0.75f, 0.75f);
        bindPlayerTexture(tileEntity.getCustomSkin());
        biped.renderAll(tileEntity.isActive(), tileEntity.getSpeedBoost());
        GlStateManager.popMatrix();


        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5f, 0.5f, 0.5f);
        GlStateManager.translate(-0.25f, 0f, -0.5f);

        // Render the sieve
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getBlockRendererDispatcher().renderBlock(ModBlocks.heavySieve.getDefaultState(), new BlockPos(0, 0, 0), tileEntity.getWorld(), renderer);
        tessellator.draw();

        ItemStack currentStack = tileEntity.getCurrentStack();
        if (currentStack != null) {
            float progress = tileEntity.getProgress();
            Block block = Block.getBlockFromItem(currentStack.getItem());
            renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
            mc.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0625f, 0.5625f, 0.0625f);
            GlStateManager.scale(0.88f, 0.5f - progress * 0.5f, 0.88f);
            mc.getBlockRendererDispatcher().renderBlock(block.getDefaultState(), new BlockPos(0, 0, 0), tileEntity.getWorld(), renderer);
            tessellator.draw();
            GlStateManager.popMatrix();
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
