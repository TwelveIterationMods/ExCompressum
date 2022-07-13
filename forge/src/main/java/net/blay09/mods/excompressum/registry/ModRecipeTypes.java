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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExCompressum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeTypes {

    public static RecipeType<CompressedHammerRecipe> COMPRESSED_HAMMER;
    public static RecipeType<ChickenStickRecipe> CHICKEN_STICK;
    public static RecipeType<HammerRecipe> HAMMER;
    public static RecipeType<GeneratedHeavySieveRecipe> GENERATED_HEAVY_SIEVE;
    public static RecipeType<HeavySieveRecipe> HEAVY_SIEVE;
    public static RecipeType<WoodenCrucibleRecipe> WOODEN_CRUCIBLE;

    public static RecipeSerializer<HeavySieveRecipe> heavySieveRecipe;
    public static RecipeSerializer<GeneratedHeavySieveRecipe> generatedHeavySieveRecipe;
    public static RecipeSerializer<CompressedHammerRecipe> compressedHammerRecipe;
    public static RecipeSerializer<HammerRecipe> hammerRecipe;
    public static RecipeSerializer<ChickenStickRecipe> chickenStickRecipe;
    public static RecipeSerializer<WoodenCrucibleRecipe> woodenCrucibleRecipe;

    public static void registerRecipeTypes() {
        COMPRESSED_HAMMER = RecipeType.register(ExCompressum.MOD_ID + ":compressed_hammer");
        CHICKEN_STICK = RecipeType.register(ExCompressum.MOD_ID + ":chicken_stick");
        HAMMER = RecipeType.register(ExCompressum.MOD_ID + ":hammer");
        GENERATED_HEAVY_SIEVE = RecipeType.register(ExCompressum.MOD_ID + ":heavy_sieve_generated");
        HEAVY_SIEVE = RecipeType.register(ExCompressum.MOD_ID + ":heavy_sieve");
        WOODEN_CRUCIBLE = RecipeType.register(ExCompressum.MOD_ID + ":wooden_crucible");
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(RegistryEvent.Register<RecipeSerializer<?>> event) {
        registerRecipeTypes();

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
