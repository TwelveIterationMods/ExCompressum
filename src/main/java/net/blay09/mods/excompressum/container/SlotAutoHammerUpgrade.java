package net.blay09.mods.excompressum.container;

import net.blay09.mods.excompressum.client.ClientProxy;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotAutoHammerUpgrade extends SlotItemHandler {

    public SlotAutoHammerUpgrade(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public TextureAtlasSprite getBackgroundSprite() {
        return inventory instanceof TileEntityAutoCompressedHammer ? ClientProxy.iconEmptyCompressedHammerSlot : ClientProxy.iconEmptyHammerSlot;
    }

}
