package net.blay09.mods.excompressum.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveMesh;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.tile.TileEntityHeavySieve;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.Session;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        String customName = getCustomName(Minecraft.getMinecraft().getSession());
        if(customName != null) {
            ExCompressum.chickenStick.setUnlocalizedName(ExCompressum.MOD_ID + ":" + customName);
        }

        ModelSieve sieve = new ModelSieve();
        ModelSieveMesh mesh = new ModelSieveMesh();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeavySieve.class, new RenderHeavySieve(sieve, mesh));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ExCompressum.heavySieve), new ItemRenderHeavySieve(sieve, mesh));

    }

    private String getCustomName(Session session) {
        String username = session.getUsername().toLowerCase();
        if(username.equals("wyld")) {
            return "chicken_stick_wyld";
        } else if(username.equals("jake_evans")) {
            return "chicken_stick_jake";
        } else if(username.equals("slowpoke101")) {
            return "chicken_stick_slow";
        }
        return null;
    }

}
