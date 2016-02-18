package net.blay09.mods.excompressum;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        if(Minecraft.getMinecraft().getSession().getUsername().equals("Wyld")) {
            ExCompressum.chickenStick.setUnlocalizedName("chicken_stick_wyld");
        }
    }

}
