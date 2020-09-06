package net.blay09.mods.excompressum.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import java.util.List;

public class VanillaPacketHandler {

    public static void sendTileEntityUpdate(TileEntity tileEntity) {
        if(tileEntity.getWorld().isRemote) {
            return;
        }
        List<? extends PlayerEntity> playerList = tileEntity.getWorld().getPlayers();
        SUpdateTileEntityPacket updatePacket = tileEntity.getUpdatePacket();
        if(updatePacket == null) {
            return;
        }
        for(Object obj : playerList) {
            ServerPlayerEntity entityPlayer = (ServerPlayerEntity) obj;
            if (Math.hypot(entityPlayer.posX - tileEntity.getPos().getX() + 0.5, entityPlayer.posZ - tileEntity.getPos().getZ() + 0.5) < 64) {
                entityPlayer.connection.sendPacket(updatePacket);
            }
        }
    }

}
