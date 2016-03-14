package net.blay09.mods.excompressum.client.render.tile;

import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.util.ResourceLocation;

public class RenderManaSieve extends RenderAutoSieve {

    public static final ResourceLocation textureFrame = new ResourceLocation(ExCompressum.MOD_ID, "textures/blocks/auto_mana_frame.png");

    @Override
    protected void bindFrameTexture() {
        bindTexture(textureFrame);
    }

}
