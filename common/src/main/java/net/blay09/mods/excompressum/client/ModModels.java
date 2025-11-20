package net.blay09.mods.excompressum.client;

import net.blay09.mods.balm.client.renderer.block.model.BalmBlockStateModelRegistrar;
import net.blay09.mods.balm.client.renderer.block.model.DeferredBlockStateModel;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.block.HeavySieveType;
import net.minecraft.resources.Identifier;

import java.util.*;

public class ModModels {
    public static Map<String, DeferredBlockStateModel> meshes = new HashMap<>();
    public static Map<HeavySieveType, DeferredBlockStateModel> sieves = new HashMap<>();

    public static DeferredBlockStateModel woodenCrucibleLiquid;

    public static void initialize(BalmBlockStateModelRegistrar models) {
        final var meshTypes = Set.of(
                "string",
                "flint",
                "copper",
                "iron",
                "gold",
                "diamond",
                "emerald",
                "netherite"
        );
        meshes = models.registerDiscriminated(meshTypes, it -> location("block/" + it + "_mesh"));
        woodenCrucibleLiquid = models.register(location("block/wooden_crucible_liquid"));
        sieves = models.registerDiscriminated(HeavySieveType.values(), it -> location("block/" + it.getSerializedName() + "_sieve"));
    }

    private static Identifier location(String path) {
        return Identifier.fromNamespaceAndPath(ExCompressum.MOD_ID, path);
    }

}
