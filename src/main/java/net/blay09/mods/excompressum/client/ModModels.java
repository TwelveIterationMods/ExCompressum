package net.blay09.mods.excompressum.client;

import com.mojang.datafixers.util.Either;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.HeavySieveType;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.SimpleModelTransform;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModModels {
    public static final Map<String, IBakedModel> meshes = new HashMap<>();

    public static IBakedModel woodenCrucibleLiquid;
    public static IBakedModel[] sieves;

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        try {
            // Static models used in TileEntityRenderer
            woodenCrucibleLiquid = loadAndBakeModel(event, location("block/wooden_crucible_liquid"));

            meshes.put("string", loadAndBakeModel(event, location("block/string_mesh")));
            meshes.put("flint", loadAndBakeModel(event, location("block/flint_mesh")));
            meshes.put("iron", loadAndBakeModel(event, location("block/iron_mesh")));
            meshes.put("diamond", loadAndBakeModel(event, location("block/diamond_mesh")));
            meshes.put("emerald", loadAndBakeModel(event, location("block/emerald_mesh")));
            meshes.put("netherite", loadAndBakeModel(event, location("block/netherite_mesh")));

            HeavySieveType[] sieveTypes = HeavySieveType.values;
            sieves = new IBakedModel[sieveTypes.length];
            for (HeavySieveType sieveType : sieveTypes) {
                sieves[sieveType.ordinal()] = loadAndBakeModel(event, location("block/" + sieveType.getString() + "_sieve"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static ResourceLocation location(String path) {
        return new ResourceLocation(ExCompressum.MOD_ID, path);
    }

    @Nullable
    private static IBakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation) {
        return loadAndBakeModel(event, resourceLocation, Collections.emptyMap());
    }

    @Nullable
    private static IBakedModel loadAndBakeModel(ModelBakeEvent event, ResourceLocation resourceLocation, Map<String, String> textureOverrides) {
        IUnbakedModel model = retexture(event.getModelLoader(), resourceLocation, textureOverrides);
        return model.bakeModel(event.getModelLoader(), ModelLoader.defaultTextureGetter(), SimpleModelTransform.IDENTITY, resourceLocation);
    }

    private static IUnbakedModel retexture(ModelBakery bakery, ResourceLocation baseModel, Map<String, String> replacedTextures) {
        Map<String, Either<RenderMaterial, String>> replacedTexturesMapped = new HashMap<>();
        for (Map.Entry<String, String> entry : replacedTextures.entrySet()) {
            replacedTexturesMapped.put(entry.getKey(), Either.left(new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(entry.getValue()))));
        }

        BlockModel blockModel = new BlockModel(baseModel, Collections.emptyList(), replacedTexturesMapped, false, BlockModel.GuiLight.FRONT, ItemCameraTransforms.DEFAULT, Collections.emptyList());

        // We have to call getTextures to initialize the parent field in the model (as that is usually done during stitching, which we're already past)
        blockModel.getTextures(bakery::getUnbakedModel, new HashSet<>());

        return blockModel;
    }

}
