package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.blay09.mods.excompressum.registry.hammer.HammerRegistry;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRegistry;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRegistry;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class ExRegistries {

    private static CompressedRecipeRegistry compressedRecipeRegistry;
    private static final ChickenStickRegistry chickenStickRegistry = new ChickenStickRegistry();
    private static final HammerRegistry hammerRegistry = new HammerRegistry();
    private static final CompressedHammerRegistry compressedHammerRegistry = new CompressedHammerRegistry();
    private static final WoodenCrucibleRegistry woodenCrucibleRegistry = new WoodenCrucibleRegistry();
    private static final HeavySieveRegistry heavySieveRegistry = new HeavySieveRegistry();

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(compressedRecipeRegistry = new CompressedRecipeRegistry(event.getDataPackRegistries().getRecipeManager()));
    }

    @SubscribeEvent
    public static void onRecipesUpdated(RecipesUpdatedEvent event) {
        compressedRecipeRegistry = new CompressedRecipeRegistry(event.getRecipeManager());
        compressedRecipeRegistry.reloadRecipes();
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
