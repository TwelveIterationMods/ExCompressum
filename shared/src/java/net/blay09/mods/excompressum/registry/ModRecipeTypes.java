package net.blay09.mods.excompressum.registry;

import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipe;
import net.blay09.mods.excompressum.registry.chickenstick.ChickenStickRecipeSerializer;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipeSerializer;
import net.blay09.mods.excompressum.registry.compressedhammer.CompressedHammerRecipe;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipe;
import net.blay09.mods.excompressum.registry.hammer.HammerRecipeSerializer;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.GeneratedHeavySieveRecipeSerializer;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipe;
import net.blay09.mods.excompressum.registry.heavysieve.HeavySieveRecipeSerializer;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipe;
import net.blay09.mods.excompressum.registry.woodencrucible.WoodenCrucibleRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeTypes {
    public static RecipeSerializer<HeavySieveRecipe> heavySieveRecipe;
    public static RecipeSerializer<GeneratedHeavySieveRecipe> generatedHeavySieveRecipe;
    public static RecipeSerializer<CompressedHammerRecipe> compressedHammerRecipe;
    public static RecipeSerializer<HammerRecipe> hammerRecipe;
    public static RecipeSerializer<ChickenStickRecipe> chickenStickRecipe;
    public static RecipeSerializer<WoodenCrucibleRecipe> woodenCrucibleRecipe;

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().registerAll(
                heavySieveRecipe = new HeavySieveRecipeSerializer(),
                generatedHeavySieveRecipe = new GeneratedHeavySieveRecipeSerializer(),
                compressedHammerRecipe = new CompressedHammerRecipeSerializer(),
                hammerRecipe = new HammerRecipeSerializer(),
                chickenStickRecipe = new ChickenStickRecipeSerializer(),
                woodenCrucibleRecipe = new WoodenCrucibleRecipeSerializer()
        );
    }
}
