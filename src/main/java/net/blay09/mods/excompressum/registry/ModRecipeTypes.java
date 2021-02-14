package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipeSerializer;
import net.blay09.mods.excompressum.registry.compressedhammer.CompresedHammerRecipeSerializer;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipe;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipe;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeSerializer;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipeSerializer;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipeSerializer;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeTypes {
    public static IRecipeSerializer<HeavySieveRecipe> heavySieveRecipe;
    public static IRecipeSerializer<GeneratedHeavySieveRecipe> generatedHeavySieveRecipe;
    public static IRecipeSerializer<CompressedHammerRecipe> compressedHammerRecipe;
    public static IRecipeSerializer<HammerRecipe> hammerRecipe;
    public static IRecipeSerializer<ChickenStickRecipe> chickenStickRecipe;
    public static IRecipeSerializer<WoodenCrucibleRecipe> woodenCrucibleRecipe;

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().registerAll(
                heavySieveRecipe = new HeavySieveRecipeSerializer(),
                generatedHeavySieveRecipe = new GeneratedHeavySieveRecipeSerializer(),
                compressedHammerRecipe = new CompresedHammerRecipeSerializer(),
                hammerRecipe = new HammerRecipeSerializer(),
                chickenStickRecipe = new ChickenStickRecipeSerializer(),
                woodenCrucibleRecipe = new WoodenCrucibleRecipeSerializer()
        );
    }
}
