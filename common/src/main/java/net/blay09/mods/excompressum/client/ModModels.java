package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.HeavySieveType;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ModModels {
    public static final Map<String, DeferredObject<BlockStateModel>> meshes = new HashMap<>();

    public static final List<DeferredObject<BlockStateModel>> sieves = new ArrayList<>();
    public static DeferredObject<BlockStateModel> woodenCrucibleLiquid;

    public static void initialize(BalmModels models) {
        woodenCrucibleLiquid = models.loadModel(location("block/wooden_crucible_liquid"));

        meshes.put("string", models.loadModel(location("block/string_mesh")));
        meshes.put("flint", models.loadModel(location("block/flint_mesh")));
        meshes.put("copper", models.loadModel(location("block/copper_mesh")));
        meshes.put("iron", models.loadModel(location("block/iron_mesh")));
        meshes.put("gold", models.loadModel(location("block/gold_mesh")));
        meshes.put("diamond", models.loadModel(location("block/diamond_mesh")));
        meshes.put("emerald", models.loadModel(location("block/emerald_mesh")));
        meshes.put("netherite", models.loadModel(location("block/netherite_mesh")));

        final var sieveTypes = HeavySieveType.values;
        for (final var sieveType : sieveTypes) {
            sieves.add(sieveType.ordinal(), models.loadModel(location("block/" + sieveType.getSerializedName() + "_sieve")));
        }
    }

    private static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, path);
    }

}
