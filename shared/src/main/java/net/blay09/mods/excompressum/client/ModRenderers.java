package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.excompressum.block.BaitBlock;
import net.blay09.mods.excompressum.block.BaitType;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.client.render.entity.AngryChickenRenderer;
import net.blay09.mods.excompressum.client.render.blockentity.*;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.blay09.mods.excompressum.block.entity.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.block.Block;

public class ModRenderers {

    public static void initialize(BalmRenderers renderers) {
        renderers.setBlockRenderType(() -> ModBlocks.autoSieve, RenderType.cutout());
        renderers.setBlockRenderType(() -> ModBlocks.autoHeavySieve, RenderType.cutout());
        renderers.setBlockRenderType(() -> ModBlocks.autoHammer, RenderType.cutout());
        renderers.setBlockRenderType(() -> ModBlocks.autoCompressedHammer, RenderType.cutout());

        renderers.registerBlockEntityRenderer(ModBlockEntities.heavySieve::get, HeavySieveRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.autoSieve::get, AutoSieveRenderer::normal);
        renderers.registerBlockEntityRenderer(ModBlockEntities.autoHeavySieve::get, AutoSieveRenderer::heavy);
        renderers.registerBlockEntityRenderer(ModBlockEntities.autoHammer::get, AutoHammerRenderer::normal);
        renderers.registerBlockEntityRenderer(ModBlockEntities.autoCompressedHammer::get, AutoHammerRenderer::compressed);
        renderers.registerBlockEntityRenderer(ModBlockEntities.woodenCrucible::get, WoodenCrucibleRenderer::new);
        renderers.registerBlockEntityRenderer(ModBlockEntities.bait::get, BaitRenderer::new);

        renderers.registerEntityRenderer(ModEntities.angryChicken::get, context -> new AngryChickenRenderer(context, new ChickenModel<>(context.bakeLayer(ModelLayers.CHICKEN)), 0.3f));

        ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof ReloadableResourceManager) {
            ((ReloadableResourceManager) resourceManager).registerReloadListener((ResourceManagerReloadListener) manager -> AutoSieveRenderer.cacheKey++);
        }

        renderers.registerItemColorHandler((itemStack, tintIndex) -> {
            Block block = Block.byItem(itemStack.getItem());
            if (block instanceof BaitBlock) {
                BaitType baitType = ((BaitBlock) block).getBaitType();
                return baitType.getItemColor(tintIndex);
            }

            return 0;
        }, () -> ModBlocks.baits);
    }
}
