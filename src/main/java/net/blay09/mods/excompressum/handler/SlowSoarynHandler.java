package net.blay09.mods.excompressum.handler;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;

/**
 * They asked for it. No, they literally did.
 * It was a joke on stream and they're both aware of it. ;)
 */
@SuppressWarnings("unused")
public class SlowSoarynHandler {

    private int scheduledHiss;

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent event) {
        if(event.gui == null) {
            return;
        }
        EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
        if(entityPlayer != null && Math.random() <= 0.001) {
            scheduledHiss = 15 + (int) (Math.random() * 80);
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(scheduledHiss > 0) {
            scheduledHiss--;
            if(scheduledHiss <= 0) {
                EntityPlayer entityPlayer = Minecraft.getMinecraft().thePlayer;
                if(entityPlayer != null) {
                    entityPlayer.worldObj.playSound(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, "creeper.primed", 1f, 0.5f, false);
                }
            }
        }
    }

}
