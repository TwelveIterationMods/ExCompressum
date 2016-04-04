package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.block.BlockAutoCompressedHammer;
import net.blay09.mods.excompressum.client.render.model.ModelAutoFrame;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
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

public class RenderAutoCompressedHammer extends RenderAutoHammer {

    private final ResourceLocation textureFrame = new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/auto_heavy_frame.png");

    @Override
    protected void bindFrameTexture() {
        bindTexture(textureFrame);
    }

    @Override
    protected ItemStack createHammerItemStack() {
        return new ItemStack(ModItems.compressedHammerDiamond);
    }
}
