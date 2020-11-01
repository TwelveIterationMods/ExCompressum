package net.blay09.mods.excompressum.client;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.client.render.entity.AngryChickenRenderer;
import net.blay09.mods.excompressum.client.render.tile.*;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.blay09.mods.excompressum.tile.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRenderers {

    public static void register() {
        RenderTypeLookup.setRenderLayer(ModBlocks.autoSieve, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.autoHeavySieve, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.autoHammer, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.autoCompressedHammer, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.manaSieve, RenderType.getCutout());

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.heavySieve, HeavySieveRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoSieve, AutoSieveRenderer::normal);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.manaSieve, AutoSieveRenderer::normal);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoHeavySieve, AutoSieveRenderer::heavy);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoHammer, AutoHammerRenderer::normal);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoCompressedHammer, AutoHammerRenderer::compressed);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.woodenCrucible, WoodenCrucibleRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.bait, BaitRenderer::new);

        final ChickenModel<AngryChickenEntity> modelChicken = new ChickenModel<>();
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.angryChicken, manager -> new AngryChickenRenderer(manager, modelChicken, 0.3f));
    }

    @SubscribeEvent
    public static void initBlockColors(ColorHandlerEvent.Block event) {
        // Guard against event bus crashes
        if (ModBlocks.woodenCrucibles != null) {
            for (Block woodenCrucible : ModBlocks.woodenCrucibles) {
                event.getBlockColors().register((state, world, pos, i) -> 4159204, woodenCrucible);
            }
        }
    }

}
