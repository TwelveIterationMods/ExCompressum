package net.blay09.mods.excompressum.handler;

import cpw.mods.fml.common.network.IGuiHandler;
import net.blay09.mods.excompressum.client.gui.GuiAutoCompressedHammer;
import net.blay09.mods.excompressum.client.gui.GuiAutoCompressor;
import net.blay09.mods.excompressum.container.ContainerAutoCompressedHammer;
import net.blay09.mods.excompressum.container.ContainerAutoCompressor;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressedHammer;
import net.blay09.mods.excompressum.tile.TileEntityAutoCompressor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_AUTO_COMPRESSED_HAMMER = 0;
    public static final int GUI_AUTO_COMPRESSOR = 1;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        switch(id) {
            case GUI_AUTO_COMPRESSED_HAMMER:
                if(tileEntity instanceof TileEntityAutoCompressedHammer) {
                    return new ContainerAutoCompressedHammer(entityPlayer.inventory, (TileEntityAutoCompressedHammer) tileEntity);
                }
                break;
            case GUI_AUTO_COMPRESSOR:
                if(tileEntity instanceof TileEntityAutoCompressor) {
                    return new ContainerAutoCompressor(entityPlayer.inventory, (TileEntityAutoCompressor) tileEntity);
                }
                break;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        switch(id) {
            case GUI_AUTO_COMPRESSED_HAMMER:
                if(tileEntity instanceof TileEntityAutoCompressedHammer) {
                    return new GuiAutoCompressedHammer(entityPlayer.inventory, (TileEntityAutoCompressedHammer) tileEntity);
                }
                break;
            case GUI_AUTO_COMPRESSOR:
                if(tileEntity instanceof TileEntityAutoCompressor) {
                    return new GuiAutoCompressor(entityPlayer.inventory, (TileEntityAutoCompressor) tileEntity);
                }
                break;
        }
        return null;
    }
}
