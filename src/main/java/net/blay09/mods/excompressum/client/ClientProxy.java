package net.blay09.mods.excompressum.client;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    private final Set<GameProfile> skinRequested = Sets.newHashSet();
    public static TextureAtlasSprite iconEmptyBookSlot;
    public static TextureAtlasSprite iconEmptyHammerSlot;
    public static TextureAtlasSprite iconEmptyCompressedHammerSlot;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        // TODO JSON FUN:
        /*ModelSieve sieve = new ModelSieve();
        ModelSieveMesh mesh = new ModelSieveMesh();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeavySieve.class, new RenderHeavySieve(sieve, mesh));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.heavySieve), new ItemRenderHeavySieve(sieve, mesh));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSieve.class, new RenderAutoSieve());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoSieve), new ItemRenderAutoSieve());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSieveMana.class, new RenderManaSieve());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.manaSieve), new ItemRenderManaSieve());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoHeavySieve.class, new RenderAutoHeavySieve());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoHeavySieve), new ItemRenderAutoHeavySieve());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenCrucible.class, new RenderWoodenCrucible());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.woodenCrucible), new ItemRenderWoodenCrucible());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoHammer.class, new RenderAutoHammer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoHammer), new ItemRenderAutoHammer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoCompressedHammer.class, new RenderAutoCompressedHammer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoCompressedHammer), new ItemRenderAutoCompressedHammer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBait.class, new RenderBait());*/

        // TODO "use the factory version", thanks, very helpful!
        //RenderingRegistry.registerEntityRenderingHandler(EntityAngryChicken.class, new RenderAngryChicken(new ModelChicken(), 0.3f));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        String customName = ChickenStickRegistry.chickenStickNames.get(Minecraft.getMinecraft().getSession().getUsername().toLowerCase());
        if(customName == null) {
            customName = ChickenStickRegistry.chickenStickNames.get("*");
        }
        if(customName != null) {
            ChickenStickRegistry.setChickenStickName(customName);
        }
    }

    @Override
    public void preloadSkin(GameProfile customSkin) {
        if(!skinRequested.contains(customSkin)) {
            Minecraft.getMinecraft().getSkinManager().loadProfileTextures(customSkin, new SkinManager.SkinAvailableCallback() {
                @Override
                public void skinAvailable(MinecraftProfileTexture.Type typeIn, ResourceLocation location, MinecraftProfileTexture profileTexture) {
                    // NOP
                }
            }, true);
            skinRequested.add(customSkin);
        }
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        if(event.getMap() == Minecraft.getMinecraft().getTextureMapBlocks()) {
            iconEmptyBookSlot = event.getMap().registerSprite(new ResourceLocation(ExCompressum.MOD_ID, "empty_enchanted_book_slot"));
            iconEmptyHammerSlot = event.getMap().registerSprite(new ResourceLocation(ExCompressum.MOD_ID, "empty_hammer_slot"));
            iconEmptyCompressedHammerSlot = event.getMap().registerSprite(new ResourceLocation(ExCompressum.MOD_ID, "empty_compressed_hammer_slot"));
        }
    }
}
