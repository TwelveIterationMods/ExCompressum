package net.blay09.mods.excompressum.client.render.item;

import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderAutoCompressedHammer implements IItemRenderer {

    private static final TileEntityAutoCompressedHammer tileEntity = new TileEntityAutoCompressedHammer();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type != ItemRenderType.FIRST_PERSON_MAP;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        TileEntityRendererDispatcher.instance.renderTileEntityAt(tileEntity, 0, 0, 0, 0);
    }

}
