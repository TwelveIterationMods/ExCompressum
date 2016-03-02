package net.blay09.mods.excompressum.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import exnihilo.blocks.models.ModelCrucible;
import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveMesh;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.client.render.item.ItemRenderAutoHeavySieve;
import net.blay09.mods.excompressum.client.render.item.ItemRenderHeavySieve;
import net.blay09.mods.excompressum.client.render.item.ItemRenderWoodenCrucible;
import net.blay09.mods.excompressum.client.render.tile.RenderAutoHeavySieve;
import net.blay09.mods.excompressum.client.render.tile.RenderBait;
import net.blay09.mods.excompressum.client.render.tile.RenderHeavySieve;
import net.blay09.mods.excompressum.client.render.tile.RenderWoodenCrucible;
import net.blay09.mods.excompressum.handler.SlowSoarynHandler;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.blay09.mods.excompressum.tile.TileEntityAutoHeavySieve;
import net.blay09.mods.excompressum.tile.TileEntityBait;
import net.blay09.mods.excompressum.tile.TileEntityHeavySieve;
import net.blay09.mods.excompressum.tile.TileEntityWoodenCrucible;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.Session;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        setupSillyThings();

        ModelSieve sieve = new ModelSieve();
        ModelSieveMesh mesh = new ModelSieveMesh();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeavySieve.class, new RenderHeavySieve(sieve, mesh));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.heavySieve), new ItemRenderHeavySieve(sieve, mesh));
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoHeavySieve.class, new RenderAutoHeavySieve(sieve, mesh));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoHeavySieve), new ItemRenderAutoHeavySieve(sieve, mesh));

        ModelCrucible model = new ModelCrucible();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenCrucible.class, new RenderWoodenCrucible(model));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.woodenCrucible), new ItemRenderWoodenCrucible(model));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBait.class, new RenderBait());
    }

    private void setupSillyThings() {
        String customName = getCustomName(Minecraft.getMinecraft().getSession());
        if(customName != null) {
            ModItems.chickenStick.setUnlocalizedName(customName);
        }

        if(!ChickenStickRegistry.chickenOut) {
            String userName = Minecraft.getMinecraft().getSession().getUsername();
            if (userName.toLowerCase().equals("soaryn") || userName.toLowerCase().equals("slowpoke101")) {
                SlowSoarynHandler handler = new SlowSoarynHandler();
                MinecraftForge.EVENT_BUS.register(handler);
                FMLCommonHandler.instance().bus().register(handler);
            }
        }
    }

    private String getCustomName(Session session) {
        String customName = ChickenStickRegistry.chickenStickNames.get(session.getUsername().toLowerCase());
        if(customName == null) {
            customName = ChickenStickRegistry.chickenStickNames.get("*");
        }
        return customName;
    }

}
