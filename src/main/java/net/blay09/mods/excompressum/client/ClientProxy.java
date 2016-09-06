package net.blay09.mods.excompressum.client;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.client.render.entity.RenderAngryChicken;
import net.blay09.mods.excompressum.client.render.tile.RenderAutoSieve;
import net.blay09.mods.excompressum.client.render.tile.RenderBait;
import net.blay09.mods.excompressum.client.render.tile.RenderHeavySieve;
import net.blay09.mods.excompressum.client.render.tile.RenderWoodenCrucible;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.blay09.mods.excompressum.tile.TileBait;
import net.blay09.mods.excompressum.tile.TileAutoSieve;
import net.blay09.mods.excompressum.tile.TileAutoSieveMana;
import net.blay09.mods.excompressum.tile.TileAutoHeavySieve;
import net.blay09.mods.excompressum.tile.TileHeavySieve;
import net.blay09.mods.excompressum.tile.TileWoodenCrucible;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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

        ClientRegistry.bindTileEntitySpecialRenderer(TileHeavySieve.class, new RenderHeavySieve());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoSieve.class, new RenderAutoSieve());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoSieveMana.class, new RenderAutoSieve());
        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoHeavySieve.class, new RenderAutoSieve());
//        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoHammer.class, new RenderAutoHammer()); // TODO uh, we don't really have the items here yet, so fix this later
//        ClientRegistry.bindTileEntitySpecialRenderer(TileAutoCompressedHammer.class, new RenderAutoHammer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileWoodenCrucible.class, new RenderWoodenCrucible());
        ClientRegistry.bindTileEntitySpecialRenderer(TileBait.class, new RenderBait());

        final ModelChicken modelChicken = new ModelChicken();
        RenderingRegistry.registerEntityRenderingHandler(EntityAngryChicken.class, new IRenderFactory<EntityAngryChicken>() {
            @Override
            public Render<? super EntityAngryChicken> createRenderFor(RenderManager manager) {
                return new RenderAngryChicken(manager, modelChicken, 0.3f);
            }
        });
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
            iconEmptyBookSlot = event.getMap().registerSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_enchanted_book_slot"));
            iconEmptyHammerSlot = event.getMap().registerSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_hammer_slot"));
            iconEmptyCompressedHammerSlot = event.getMap().registerSprite(new ResourceLocation(ExCompressum.MOD_ID, "items/empty_compressed_hammer_slot"));
        }
    }
}
