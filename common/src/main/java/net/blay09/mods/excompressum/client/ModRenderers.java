package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.blay09.mods.excompressum.client.render.blockentity.*;
import net.blay09.mods.excompressum.client.render.entity.AngryChickenRenderer;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import static net.blay09.mods.excompressum.ExCompressum.id;

public class ModRenderers {

    public static void initialize(BalmRenderers renderers) {
        renderers.setBlockRenderType(() -> ModBlocks.autoSieve, RenderType.cutout());
        renderers.setBlockRenderType(() -> ModBlocks.autoHeavySieve, RenderType.cutout());
        renderers.setBlockRenderType(() -> ModBlocks.autoHammer, RenderType.cutout());
        renderers.setBlockRenderType(() -> ModBlocks.autoCompressedHammer, RenderType.cutout());

        renderers.registerBlockEntityRenderer(id("heavy_sieve"), ModBlockEntities.heavySieve::get, HeavySieveRenderer::new);
        renderers.registerBlockEntityRenderer(id("auto_sieve"), ModBlockEntities.autoSieve::get, AutoSieveRenderer::normal);
        renderers.registerBlockEntityRenderer(id("auto_heavy_sieve"), ModBlockEntities.autoHeavySieve::get, AutoSieveRenderer::heavy);
        renderers.registerBlockEntityRenderer(id("auto_hammer"), ModBlockEntities.autoHammer::get, AutoHammerRenderer::normal);
        renderers.registerBlockEntityRenderer(id("auto_compressed_hammer"), ModBlockEntities.autoCompressedHammer::get, AutoHammerRenderer::compressed);
        renderers.registerBlockEntityRenderer(id("wooden_crucible"), ModBlockEntities.woodenCrucible::get, WoodenCrucibleRenderer::new);
        renderers.registerBlockEntityRenderer(id("bait"), ModBlockEntities.bait::get, BaitRenderer::new);

        renderers.registerEntityRenderer(id("angry_chicken"),
                ModEntities.angryChicken::get,
                context -> new AngryChickenRenderer(context, new ChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3f));

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager) {
            ((ReloadableResourceManager) resourceManager).registerReloadListener((ResourceManagerReloadListener) manager -> AutoSieveRenderer.cacheKey++);
        }
    }
}
