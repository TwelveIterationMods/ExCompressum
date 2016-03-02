package net.blay09.mods.excompressum.client.render.item;

import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveMesh;
import net.blay09.mods.excompressum.client.render.tile.RenderAutoHeavySieve;
import net.minecraft.client.Minecraft;

public class ItemRenderAutoHeavySieve extends ItemRenderHeavySieve {

    public ItemRenderAutoHeavySieve(ModelSieve model, ModelSieveMesh mesh) {
        super(model, mesh);
    }

    @Override
    protected void bindTexture(int metadata) {
        if (metadata >= 0) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(RenderAutoHeavySieve.texture);
        }
    }

}
