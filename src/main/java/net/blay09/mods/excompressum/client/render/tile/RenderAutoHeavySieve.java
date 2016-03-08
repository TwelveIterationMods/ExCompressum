package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.util.ResourceLocation;

public class RenderAutoHeavySieve extends RenderAutoSieve {

    public static final ResourceLocation textureFrame = new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/auto_heavy_frame.png");
    public static final ResourceLocation textureSieve = new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/heavy_sieve_oak.png");

    @Override
    protected void bindFrameTexture() {
        bindTexture(textureFrame);
    }

    @Override
    protected void bindSieveTexture() {
        bindTexture(textureSieve);
    }
}
