package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.excompressum.ExCompressum;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class ModModels {
    public static final Map<String, DeferredObject<BlockStateModel>> meshes = new HashMap<>();

    public static final List<DeferredObject<BlockStateModel>> sieves = new ArrayList<>();
    public static DeferredObject<BlockStateModel> woodenCrucibleLiquid;

    public static void initialize(BalmModels models) {
        // TODO woodenCrucibleLiquid = models.loadModel(location("block/wooden_crucible_liquid"));
// TODO
        // TODO meshes.put("string", models.loadModel(location("block/string_mesh")));
        // TODO meshes.put("flint", models.loadModel(location("block/flint_mesh")));
        // TODO meshes.put("copper", models.loadModel(location("block/copper_mesh")));
        // TODO meshes.put("iron", models.loadModel(location("block/iron_mesh")));
        // TODO meshes.put("gold", models.loadModel(location("block/gold_mesh")));
        // TODO meshes.put("diamond", models.loadModel(location("block/diamond_mesh")));
        // TODO meshes.put("emerald", models.loadModel(location("block/emerald_mesh")));
        // TODO meshes.put("netherite", models.loadModel(location("block/netherite_mesh")));
// TODO
        // TODO HeavySieveType[] sieveTypes = HeavySieveType.values;
        // TODO for (HeavySieveType sieveType : sieveTypes) {
        // TODO     sieves.add(sieveType.ordinal(), models.loadModel(location("block/" + sieveType.getSerializedName() + "_sieve")));
        // TODO }
    }

    private static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(ExCompressum.MOD_ID, path);
    }

}
