package net.blay09.mods.excompressum.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import exnihilo.blocks.models.ModelCrucible;
import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveMesh;
import exnihilo.blocks.tileentities.TileEntityCrucible;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.handler.SlowSoarynHandler;
import net.blay09.mods.excompressum.tile.TileEntityBait;
import net.blay09.mods.excompressum.tile.TileEntityHeavySieve;
import net.blay09.mods.excompressum.tile.TileEntityWoodenCrucible;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.Session;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        setupSillyThings();

        ModelSieve sieve = new ModelSieve();
        ModelSieveMesh mesh = new ModelSieveMesh();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeavySieve.class, new RenderHeavySieve(sieve, mesh));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ExCompressum.heavySieve), new ItemRenderHeavySieve(sieve, mesh));

        ModelCrucible model = new ModelCrucible();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenCrucible.class, new RenderWoodenCrucible(model));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ExCompressum.woodenCrucible), new ItemRenderWoodenCrucible(model));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBait.class, new RenderBait());
    }

    private void setupSillyThings() {
        String customName = getCustomName(Minecraft.getMinecraft().getSession());
        if(customName != null) {
            ExCompressum.chickenStick.setUnlocalizedName(customName);
        }

        if(!ExCompressum.chickenOut) {
            String userName = Minecraft.getMinecraft().getSession().getUsername();
            if (userName.toLowerCase().equals("soaryn") || userName.toLowerCase().equals("slowpoke101")) {
                SlowSoarynHandler handler = new SlowSoarynHandler();
                MinecraftForge.EVENT_BUS.register(handler);
                FMLCommonHandler.instance().bus().register(handler);
            }
        }
    }

    private String getCustomName(Session session) {
        String customName = ExCompressum.chickenStickNames.get(session.getUsername().toLowerCase());
        if(customName == null) {
            customName = ExCompressum.chickenStickNames.get("*");
        }
        return customName;
    }

}
