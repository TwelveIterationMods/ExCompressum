package net.blay09.mods.excompressum.registry;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.RecipesUpdatedEvent;
import net.blay09.mods.balm.api.event.server.ServerReloadFinishedEvent;
import net.blay09.mods.balm.api.event.server.ServerStartedEvent;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.hammer.HammerRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.sievemesh.SieveMeshRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;

public class ExRegistries {

    private static final CompressedRecipeRegistry compressedRecipeRegistry = new CompressedRecipeRegistry();
    private static final ChickenStickRegistry chickenStickRegistry = new ChickenStickRegistry();
    private static final HammerRegistry hammerRegistry = new HammerRegistry();
    private static final CompressedHammerRegistry compressedHammerRegistry = new CompressedHammerRegistry();
    private static final WoodenCrucibleRegistry woodenCrucibleRegistry = new WoodenCrucibleRegistry();
    private static final HeavySieveRegistry heavySieveRegistry = new HeavySieveRegistry();

    public static void initialize() {
        Balm.getEvents()
                .onEvent(ServerStartedEvent.class,
                        it -> compressedRecipeRegistry.reloadRecipes(it.getServer().getRecipeManager(), it.getServer().registryAccess()));
        Balm.getEvents()
                .onEvent(ServerReloadFinishedEvent.class,
                        it -> compressedRecipeRegistry.reloadRecipes(it.getServer().getRecipeManager(), it.getServer().registryAccess()));
        Balm.getEvents().onEvent(RecipesUpdatedEvent.class, ExRegistries::onRecipesUpdated);
    }

    public static void onRecipesUpdated(RecipesUpdatedEvent event) {
        compressedRecipeRegistry.reloadRecipes(event.getRecipeManager(), event.getRegistryAccess());
    }

    public static CompressedRecipeRegistry getCompressedRecipeRegistry() {
        return compressedRecipeRegistry;
    }

    public static ChickenStickRegistry getChickenStickRegistry() {
        return chickenStickRegistry;
    }

    public static HammerRegistry getHammerRegistry() {
        return hammerRegistry;
    }

    public static CompressedHammerRegistry getCompressedHammerRegistry() {
        return compressedHammerRegistry;
    }

    public static WoodenCrucibleRegistry getWoodenCrucibleRegistry() {
        return woodenCrucibleRegistry;
    }

    public static HeavySieveRegistry getHeavySieveRegistry() {
        return heavySieveRegistry;
    }
}
