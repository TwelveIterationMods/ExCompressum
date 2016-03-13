package net.blay09.mods.excompressum.handler;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class VanillaPacketHandler {

    public static void sendTileEntityUpdate(TileEntity tileEntity) {
        if(tileEntity.getWorldObj().isRemote) {
            return;
        }
        List playerList = tileEntity.getWorldObj().playerEntities;
        for(Object obj : playerList) {
            EntityPlayerMP entityPlayer = (EntityPlayerMP) obj;
            if (Math.hypot(entityPlayer.posX - tileEntity.xCoord + 0.5, entityPlayer.posZ - tileEntity.zCoord + 0.5) < 64) {
                entityPlayer.playerNetServerHandler.sendPacket(tileEntity.getDescriptionPacket());
            }
        }
    }

}
