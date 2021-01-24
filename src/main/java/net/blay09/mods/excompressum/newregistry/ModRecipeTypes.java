package net.blay09.mods.excompressum.newregistry;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.newregistry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.newregistry.chickenstick.ChickenStickRecipeSerializer;
import net.blay09.mods.excompressum.newregistry.compressedhammer.CompresedHammerRecipeSerializer;
import net.blay09.mods.excompressum.newregistry.compressedhammer.CompressedHammerRecipe;
import net.blay09.mods.excompressum.newregistry.hammer.HammerRecipe;
import net.blay09.mods.excompressum.newregistry.hammer.HammerRecipeSerializer;
import net.blay09.mods.excompressum.newregistry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.newregistry.heavysieve.GeneratedHeavySieveRecipeSerializer;
import net.blay09.mods.excompressum.newregistry.heavysieve.HeavySieveRecipe;
import net.blay09.mods.excompressum.newregistry.heavysieve.HeavySieveRecipeSerializer;
import net.blay09.mods.excompressum.newregistry.woodencrucible.WoodenCrucibleRecipe;
import net.blay09.mods.excompressum.newregistry.woodencrucible.WoodenCrucibleRecipeSerializer;
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
    public static IRecipeSerializer<ChickenStickRecipe> chickenStick;
    public static IRecipeSerializer<WoodenCrucibleRecipe> woodenCrucible;

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().registerAll(
                heavySieveRecipe = new HeavySieveRecipeSerializer(),
                generatedHeavySieveRecipe = new GeneratedHeavySieveRecipeSerializer(),
                compressedHammerRecipe = new CompresedHammerRecipeSerializer(),
                hammerRecipe = new HammerRecipeSerializer(),
                chickenStick = new ChickenStickRecipeSerializer(),
                woodenCrucible = new WoodenCrucibleRecipeSerializer()
        );
    }
}
