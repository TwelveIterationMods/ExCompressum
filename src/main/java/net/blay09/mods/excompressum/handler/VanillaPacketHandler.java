package net.blay09.mods.excompressum.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class VanillaPacketHandler {

    public static void sendTileEntityUpdate(TileEntity tileEntity) {
        if(tileEntity.getWorld().isRemote) {
            return;
        }
        List playerList = tileEntity.getWorld().playerEntities;
        SPacketUpdateTileEntity updatePacket = tileEntity.getUpdatePacket();
        if(updatePacket == null) {
            return;
        }
        for(Object obj : playerList) {
            EntityPlayerMP entityPlayer = (EntityPlayerMP) obj;
            if (Math.hypot(entityPlayer.posX - tileEntity.getPos().getX() + 0.5, entityPlayer.posZ - tileEntity.getPos().getZ() + 0.5) < 64) {
                entityPlayer.connection.sendPacket(updatePacket);
            }
        }
    }

}
