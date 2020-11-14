package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRegistry;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRegistry;
import net.blay09.mods.excompressum.registry.compressor.CompressedRecipeRegistry;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID)
public class ExRegistries {

    private static CompressedRecipeRegistry compressedRecipeRegistry;
    private static ChickenStickRegistry chickenStickRegistry;
    private static CompressedHammerRegistry compressedHammerRegistry;

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(compressedRecipeRegistry = new CompressedRecipeRegistry(event.getDataPackRegistries()));
        event.addListener(chickenStickRegistry = new ChickenStickRegistry());
        event.addListener(compressedHammerRegistry = new CompressedHammerRegistry());
    }

    public static CompressedRecipeRegistry getCompressedRecipeRegistry() {
        return compressedRecipeRegistry;
    }

    public static ChickenStickRegistry getChickenStickRegistry() {
        return chickenStickRegistry;
    }

    public static CompressedHammerRegistry getCompressedHammerRegistry() {
        return compressedHammerRegistry;
    }
}
