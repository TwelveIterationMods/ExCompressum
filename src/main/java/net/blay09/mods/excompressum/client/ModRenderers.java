package net.blay09.mods.excompressum.client;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.ModBlocks;
import net.blay09.mods.excompressum.client.render.entity.AngryChickenRenderer;
import net.blay09.mods.excompressum.client.render.tile.*;
import net.blay09.mods.excompressum.entity.AngryChickenEntity;
import net.blay09.mods.excompressum.entity.ModEntities;
import net.blay09.mods.excompressum.tile.AutoSieveTileEntityBase;
import net.blay09.mods.excompressum.tile.ModTileEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.model.ChickenModel;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRenderers {

    @SuppressWarnings("unchecked")
    public static void register() {
        RenderTypeLookup.setRenderLayer(ModBlocks.autoSieve, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.autoHeavySieve, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.autoHammer, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.autoCompressedHammer, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.manaSieve, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(ModBlocks.evolvedOrechid, RenderType.getCutout());

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.heavySieve, HeavySieveRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoSieve, AutoSieveRenderer::normal);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoHeavySieve, AutoSieveRenderer::heavy);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoHammer, AutoHammerRenderer::normal);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.autoCompressedHammer, AutoHammerRenderer::compressed);

        ClientRegistry.bindTileEntityRenderer(ModTileEntities.woodenCrucible, WoodenCrucibleRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ModTileEntities.bait, BaitRenderer::new);

        final ChickenModel<AngryChickenEntity> angryChickenModel = new ChickenModel<>();
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.angryChicken, manager -> new AngryChickenRenderer(manager, angryChickenModel, 0.3f));

        IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).addReloadListener((IResourceManagerReloadListener) manager -> AutoSieveRenderer.cacheKey++);
        }

        ClientRegistry.bindTileEntityRenderer((TileEntityType<AutoSieveTileEntityBase>) ModTileEntities.manaSieve, AutoSieveRenderer::normal);
    }

}
