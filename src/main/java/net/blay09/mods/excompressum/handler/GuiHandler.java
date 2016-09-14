package net.blay09.mods.excompressum.handler;

import net.blay09.mods.excompressum.client.gui.GuiAutoHammer;
import net.blay09.mods.excompressum.client.gui.GuiAutoCompressor;
import net.blay09.mods.excompressum.client.gui.GuiAutoSieve;
import net.blay09.mods.excompressum.client.gui.GuiManaSieve;
import net.blay09.mods.excompressum.container.ContainerAutoHammer;
import net.blay09.mods.excompressum.container.ContainerAutoCompressor;
import net.blay09.mods.excompressum.container.ContainerAutoSieve;
import net.blay09.mods.excompressum.tile.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
    public static final int GUI_AUTO_HAMMER = 0;
    public static final int GUI_AUTO_COMPRESSOR = 1;
    public static final int GUI_AUTO_SIEVE = 2;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch(id) {
            case GUI_AUTO_HAMMER:
                if(tileEntity instanceof TileAutoHammer) {
                    return new ContainerAutoHammer(entityPlayer.inventory, (TileAutoHammer) tileEntity);
                }
                break;
            case GUI_AUTO_COMPRESSOR:
                if(tileEntity instanceof TileEntityAutoCompressor) {
                    return new ContainerAutoCompressor(entityPlayer.inventory, (TileEntityAutoCompressor) tileEntity);
                }
                break;
            case GUI_AUTO_SIEVE:
                if(tileEntity instanceof TileAutoSieveBase) {
                    return new ContainerAutoSieve(entityPlayer.inventory, (TileAutoSieveBase) tileEntity);
                }
                break;
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer entityPlayer, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
        switch(id) {
            case GUI_AUTO_HAMMER:
                if(tileEntity instanceof TileAutoHammer) {
                    return new GuiAutoHammer(entityPlayer.inventory, (TileAutoHammer) tileEntity);
                }
                break;
            case GUI_AUTO_COMPRESSOR:
                if(tileEntity instanceof TileEntityAutoCompressor) {
                    return new GuiAutoCompressor(entityPlayer.inventory, (TileEntityAutoCompressor) tileEntity);
                }
                break;
            case GUI_AUTO_SIEVE:
                if(tileEntity instanceof TileAutoSieve) {
                    return new GuiAutoSieve(entityPlayer.inventory, (TileAutoSieveBase) tileEntity);
                } else if(tileEntity instanceof TileAutoSieveMana) {
                    return new GuiManaSieve(entityPlayer.inventory, (TileAutoSieveMana) tileEntity);
                }
                break;
        }
        return null;
    }
}
