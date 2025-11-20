package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.client.renderer.blockentity.BalmBlockEntityRendererRegistrar;
import net.blay09.mods.balm.client.renderer.chunk.BalmBlockRenderTypeRegistrar;
import net.blay09.mods.balm.client.renderer.entity.BalmEntityRendererRegistrar;
import net.blay09.mods.balm.server.packs.resources.BalmClientResourceReloadListenerRegistrar;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.client.render.blockentity.*;
import net.blay09.mods.excompressum.client.render.entity.AngryChickenRenderer;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.minecraft.client.model.animal.chicken.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public class ModRenderers {

    public static void initialize(BalmBlockRenderTypeRegistrar renderers) {
        renderers.setRenderLayer(ModBlocks.autoSieve, ChunkSectionLayer.CUTOUT);
        renderers.setRenderLayer(ModBlocks.autoHeavySieve, ChunkSectionLayer.CUTOUT);
        renderers.setRenderLayer(ModBlocks.autoHammer, ChunkSectionLayer.CUTOUT);
        renderers.setRenderLayer(ModBlocks.autoCompressedHammer, ChunkSectionLayer.CUTOUT);
    }

    public static void initialize(BalmBlockEntityRendererRegistrar renderers) {
        renderers.register(ModBlockEntities.heavySieve, HeavySieveRenderer::new);
        renderers.register(ModBlockEntities.autoSieve, AutoSieveRenderer::normal);
        renderers.register(ModBlockEntities.autoHeavySieve, AutoSieveRenderer::heavy);
        renderers.register(ModBlockEntities.autoHammer, AutoHammerRenderer::normal);
        renderers.register(ModBlockEntities.autoCompressedHammer, AutoHammerRenderer::compressed);
        renderers.register(ModBlockEntities.woodenCrucible, WoodenCrucibleRenderer::new);
        renderers.register(ModBlockEntities.bait, BaitRenderer::new);
    }

    public static void initialize(BalmEntityRendererRegistrar renderers) {
        renderers.register(ModEntities.angryChicken, context -> new AngryChickenRenderer(context, new ChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3f));
    }

    public static void initialize(BalmClientResourceReloadListenerRegistrar renderers) {
        renderers.register("cache_invalidation", (ResourceManagerReloadListener) manager -> AutoSieveRenderer.cacheKey++);
    }
}
